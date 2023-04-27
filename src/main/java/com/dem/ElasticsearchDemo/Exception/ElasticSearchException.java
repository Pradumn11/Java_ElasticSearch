package com.dem.ElasticsearchDemo.Exception;


import org.springframework.web.bind.annotation.ControllerAdvice;

public class ElasticSearchException extends  RuntimeException{

    String errorMessage;
    String errorCode;

    public ElasticSearchException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;

    }
    public ElasticSearchException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
