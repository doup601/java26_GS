package br.com.fiap3esph.trajetoria.exception;

import br.com.fiap3esph.trajetoria.exception.type.JovemNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.OngNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.PecaNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.PedidoNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.UsuarioNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarGenericNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosBadRequest::new));
    }

    @ExceptionHandler(OngNotFoundException.class)
    public ResponseEntity<DadosMessage> tratarOngNotFound(OngNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(JovemNotFoundException.class)
    public ResponseEntity<DadosMessage> tratarJovemNotFound(JovemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(PecaNotFoundException.class)
    public ResponseEntity<DadosMessage> tratarPecaNotFound(PecaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<DadosMessage> tratarPedidoNotFound(PedidoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<DadosMessage> tratarUsuarioNotFound(UsuarioNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosMessage> tratarValidacaoException(ValidacaoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosMessage(e.getMessage()));
    }

    private record DadosBadRequest(String campo, String mensagem) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosMessage(String mensagem) {
    }
}
