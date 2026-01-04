package gabriel.bankingapp.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String[] PUBLIC_PATHS = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Verifica se o caminho é público (ex: /auth/login)
            boolean isPublicPath = Arrays.stream(PUBLIC_PATHS)
                    .anyMatch(path -> pathMatcher.match(path, request.getServletPath()));

            if (isPublicPath) {
                // Se for público, não tentes validar um JWT.
                // Apenas continua para o próximo filtro.
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Se NÃO for público, tenta obter o JWT
            String jwt = getJwtFromRequest(request);

            // 3. Valida o JWT
            // (Verifica se o user já não está autenticado)
            if (jwt != null && jwtTokenProvider.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 4. Se o token for válido, obtém o username (email)
                String username = jwtTokenProvider.getUsernameFromJWT(jwt);

                // 5. Carrega os detalhes do user a partir da BD
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 6. Cria um objeto de "Autenticação"
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Define o utilizador como "logado" no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // (Em produção, loga o erro)
            System.err.println("Não foi possível definir a autenticação do user: " + ex.getMessage());
        }

        // 8. Continua para o próximo filtro
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token "Bearer" do header Authorization
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Retorna apenas o token
        }
        return null;
    }
}
