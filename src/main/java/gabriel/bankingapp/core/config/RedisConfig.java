package gabriel.bankingapp.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching  // Ativa o caching
public class RedisConfig {

    /**
     * Configura o RedisTemplate para usar JSON em vez do binário default.
     * Isto é usado para operações manuais (ex: OTPs).
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Configura o GESTOR DE CACHE (@Cacheable).
     * Isto diz ao Spring como se deve comportar quando encontra @Cacheable.
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // Configuração 1: Como serializar os valores (usar JSON)
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        // Configuração 2: Como serializar as chaves (usar Strings)
        RedisSerializationContext.SerializationPair<String> stringSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());

        // Junta tudo na configuração por defeito do cache
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                // Define o tempo de expiração (TTL) por defeito.
                // Ex: 10 minutos. Ajusta conforme necessário.
                .entryTtl(Duration.ofMinutes(10))

                // Define o serializador da chave
                .serializeKeysWith(stringSerializer)

                // Define o serializador do valor
                .serializeValuesWith(jsonSerializer)

                // (Opcional) Não guardar valores nulos no cache
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }
}