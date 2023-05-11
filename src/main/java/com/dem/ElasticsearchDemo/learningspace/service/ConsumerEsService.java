package com.dem.ElasticsearchDemo.learningspace.service;

import com.dem.ElasticsearchDemo.learningspace.dao.ConsumerEsDao;
import com.dem.ElasticsearchDemo.learningspace.request.GetAllConsumerRequest;
import com.dem.ElasticsearchDemo.learningspace.response.ConsumerPreviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionStage;

@Service
public class ConsumerEsService {

    private final ConsumerEsDao consumerEsDao;

    @Autowired
    public ConsumerEsService(ConsumerEsDao consumerEsDao) {
        this.consumerEsDao = consumerEsDao;
    }


    public CompletionStage<ConsumerPreviewResponse>getAllConsumerByField(GetAllConsumerRequest request){
        return consumerEsDao.searchAllConsumerByFields(request)
                .thenApply(consumerResponses -> ConsumerPreviewResponse.builder().consumers(consumerResponses).build());
    }
}
