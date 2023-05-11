package com.dem.ElasticsearchDemo.learningspace.controller;

import com.dem.ElasticsearchDemo.Exception.ElasticSearchException;
import com.dem.ElasticsearchDemo.learningspace.service.ConsumerEsService;
import com.dem.ElasticsearchDemo.learningspace.dao.ConsumerEsDao;
import com.dem.ElasticsearchDemo.learningspace.request.AddConsumerRequest;
import com.dem.ElasticsearchDemo.learningspace.request.GetAllConsumerRequest;
import com.dem.ElasticsearchDemo.learningspace.response.ConsumerPreviewResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

@RestController
public class ConsumerController {

private final ConsumerEsDao consumerEsDao;
private final ConsumerEsService consumerEsService;

    public ConsumerController(ConsumerEsDao consumerEsDao, ConsumerEsService consumerEsService) {
        this.consumerEsDao = consumerEsDao;
        this.consumerEsService = consumerEsService;
    }

    @PostMapping("/addEsConsumer")
    public CompletionStage<Void>addConsumer(@RequestBody AddConsumerRequest addConsumerRequest){
        return consumerEsDao.addConsumer(addConsumerRequest)
                .exceptionally(t->{
                    throw new ElasticSearchException(t.getMessage());
                });
    }

    @PostMapping("/searchAllConsumers")
    public CompletionStage<ConsumerPreviewResponse>getConsumer(@RequestBody GetAllConsumerRequest request){
        return consumerEsService.getAllConsumerByField(request)
                .exceptionally(t->{
                    throw new ElasticSearchException(t.getMessage());
                });
    }

}
