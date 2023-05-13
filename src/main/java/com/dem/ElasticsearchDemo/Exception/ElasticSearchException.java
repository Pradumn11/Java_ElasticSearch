package com.dem.ElasticsearchDemo.Exception;


import com.dem.ElasticsearchDemo.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

public class ElasticSearchException extends  RuntimeException{

    String errorMessage;
    ErrorCode errorCode;
    HttpStatus status;

    public ElasticSearchException(String errorMessage, ErrorCode errorCode, HttpStatus status) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.status=status;

    }
    public ElasticSearchException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
