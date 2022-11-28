package com.db.tradestore.exception;

import com.db.tradestore.model.Error;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

  @ExceptionHandler(InvalidRequestBodyException.class)
  public ResponseEntity<Error> handleInvalidBodyException(
      InvalidRequestBodyException ex) {
    return new ResponseEntity(
        new Error(ErrorConstants.INVALID_REQUEST_BODY_ERROR, ErrorConstants.INVALID_REQUEST_BODY_MSG),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidTradeVersionException.class)
  public ResponseEntity<Error> handleInvalidVersionException(
      InvalidTradeVersionException ex) {
    return new ResponseEntity(
        new Error(ErrorConstants.INVALID_TRADE_ERROR, ErrorConstants.INVALID_TRADE_MSG),
        HttpStatus.CONFLICT);
  }
}
