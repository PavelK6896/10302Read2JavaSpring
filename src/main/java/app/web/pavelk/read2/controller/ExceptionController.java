package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> userAlreadyExists(Exception e) {
        log.error(e.getMessage() + " Такой пользователь уже существует");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SpringRedditException.class)
    public ResponseEntity<String> springRedditException(Exception e) {
        log.error(e.getMessage() + " Ошибка");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(Exception e) {
        log.error(e.getMessage() + " Ошибка ");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsException(Exception e) {
        log.error(e.getMessage() + " Неверный логин или пароль");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> invalidTokenException(Exception e) {
        log.error(e.getMessage() + " невалидный токен");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> postNotFoundException(Exception e) {
        log.error(e.getMessage() + " пост не найден");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundException(Exception e) {
        log.error(e.getMessage() + " пользователь не найден");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SubredditNotFoundException.class)
    public ResponseEntity<String> subredditNotFoundException(Exception e) {
        log.error(e.getMessage() + " Подразделение не найдено");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> nullPointerException(Exception e) {
        log.error(e.getMessage() + " данные не найдены");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VoteException.class)
    public ResponseEntity<String> voteException(Exception e) {
        log.error(e.getMessage() + " уже проголосовали");
        return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(Exception e) {
        log.error(e.getLocalizedMessage() + " ошибка параметры");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("validation failed");
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<String> dataException(Exception e) {
        log.error(e.getMessage() + " error data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error data");
    }

}
