package com.dem.ElasticsearchDemo.utils;

import com.dem.ElasticsearchDemo.Exception.ElasticSearchException;
import com.dem.ElasticsearchDemo.enums.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ObjectUtils {

    public static void ValidateData(boolean check, Object errorMsg){
        if (!check){
            throw new ElasticSearchException(String.valueOf(errorMsg), ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }
}
