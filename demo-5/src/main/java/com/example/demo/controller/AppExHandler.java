package com.example.demo.controller;

import com.example.demo.exception.BookNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// hey spring if any ex coming in my project controll should transfer AppExHandler
@RestControllerAdvice //ThrowsAdvice
public class AppExHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorDetails> handle404(BookNotFoundException ex){
        ErrorDetails errorDetails=
                ErrorDetails.builder().errorCode(404)
                        .timestamp(java.time.LocalDateTime.now())
                        .toContact("digvijay@gmail.com")
                        .errorMessage(ex.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDetails> handle500(Exception ex){
//        ErrorDetails errorDetails=
//                ErrorDetails.builder().errorCode(500)
//                        .timestamp(java.time.LocalDateTime.now())
//                        .toContact("naveen@ora.com")
//                        .errorMessage("pls try after some time").build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
//    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handle400(MethodArgumentNotValidException ex){

        String errorMessage= ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));


        ErrorDetails errorDetails=
                ErrorDetails.builder().errorCode(400)
                        .timestamp(java.time.LocalDateTime.now())
                        .toContact("naveen@ora.com")
                        .errorMessage(errorMessage).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
