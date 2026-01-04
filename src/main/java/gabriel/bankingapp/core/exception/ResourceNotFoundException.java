package gabriel.bankingapp.core.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exceção customizada para ser lançada quando um recurso (ex: User, Account)
 * não é encontrado na base de dados.
 *
 * A anotação @ResponseStatus garante que qualquer Controller que lance esta
 * exceção irá automaticamente retornar um erro HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        //Guarda a mensagem de erro no construtor da classe pai
        super(message);
    }
}
