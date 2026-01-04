package gabriel.bankingapp.core.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice   //Isto escuta todos os RestControllers
public class GlobalExceptionHandler {
    /**
     * Apanha erros de violaçao de contraints da BD.
     * */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex){
        //Aplicar depois uma logica mais inteligente em que vejo se foi o NIF ou o Email, etc.

        String message = "Error in the integrity of the data. The NIF or Email has already been used.";

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }


    /**
     * Esta vai apanhar erros de validao dos DTO (@Valid).
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        //Itera sobre todos os erros de campo que o @Valid do DTO encontrou
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<String> handleInvalidOtpException(InvalidOtpException ex){
        String errorMessage = ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    /**
     * Apanha qualquer outra exception que nao foi tratada.
     * Catch-All de segurança
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex){
        //Nao posso expor mensagens da DB, pois pode ter detalhes sensiveis, da DB
        ex.printStackTrace(); //Faz o log do erro completo na consola para debug

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error occurred, Try Again.");
    }


    //Começar a pensar em diferentes Exceptions, como o InsufficientFunds
}
