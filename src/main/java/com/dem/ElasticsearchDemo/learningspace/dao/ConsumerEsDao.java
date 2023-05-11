package com.dem.ElasticsearchDemo.learningspace.dao;

import com.dem.ElasticsearchDemo.Exception.ElasticSearchException;
import com.dem.ElasticsearchDemo.Model.School;
import com.dem.ElasticsearchDemo.elasticSearchDao.EsAbstractDao;
import com.dem.ElasticsearchDemo.learningspace.request.AddConsumerRequest;
import com.dem.ElasticsearchDemo.learningspace.request.GetAllConsumerRequest;
import com.dem.ElasticsearchDemo.learningspace.response.ConsumerResponse;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Repository
public class ConsumerEsDao extends EsAbstractDao {

    private final String searchTimeout;
    private final String INDEX_CONSUMER="consumer";
    private final Integer defaultPageSize;
    private final String FIRST_NAME="firstName";
    private final String LAST_NAME="lastName";
    private final String CITY="city";
    private final String STATE="state";
    private final String ADDRESS="address";
    private final String MOBILE_NUMBER="mobileNumber";
    private final String EMAIL="email";
    private final String STATUS="status";
    private final String USERID="userId";
    private final String TENANT_ID="tenantId";
    private final Gson gson;
    public ConsumerEsDao(@Qualifier("demo.restClient") RestHighLevelClient esClient) {
        super(esClient);
        this.defaultPageSize=10;
        this.searchTimeout="300s";
        this.gson=new Gson();
    }

    public CompletionStage<Void>addConsumer(AddConsumerRequest request){
        String id=INDEX_CONSUMER+"_"+request.getUserId();
        String data=gson.toJson(request);
        return addDocumentToIndex(INDEX_CONSUMER,id,data)
                .thenAccept(indexResponse -> System.out.println("Index: "+indexResponse.getResult()));
    }
    public CompletionStage<List<ConsumerResponse>>searchAllConsumerByFields(GetAllConsumerRequest request){
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        request.getFields().entrySet()
                .forEach(field-> queryBuilder.must(QueryBuilders.matchQuery(field.getKey(),field.getValue())));

        Integer offset = request.getPageNumber() * request.getPageSize();
        SearchRequest searchRequest = new SearchRequest(INDEX_CONSUMER);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(queryBuilder)
                .from(offset)
                .size(request.getPageSize())
                .timeout(TimeValue.parseTimeValue(searchTimeout, "timeout"));
        searchRequest.source(builder);

        return searchEsQueryResponse(searchRequest)
                .thenApply(this::getConsumerHits)
                .exceptionally(throwable -> {
                    throw new ElasticSearchException(throwable.getMessage());
                });
    }


    public CompletionStage<List<ConsumerResponse>>searchAllConsumerByEmail(String email,String tenantId){
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        queryBuilder.must(QueryBuilders.termQuery(EMAIL,email))
                .must(QueryBuilders.termQuery(TENANT_ID, tenantId));

        SearchRequest searchRequest = new SearchRequest(INDEX_CONSUMER);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(queryBuilder);
        searchRequest.source(builder);

        return searchEsQueryResponse(searchRequest)
                .thenApply(this::getConsumerHits)
                .exceptionally(throwable -> {
                    throw new ElasticSearchException(throwable.getMessage());
                });
    }

    public CompletionStage<List<ConsumerResponse>>searchAllConsumerByState(String state,String tenantId){
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        queryBuilder.must(QueryBuilders.termQuery(STATE,state))
                .must(QueryBuilders.termQuery(TENANT_ID, tenantId));

        SearchRequest searchRequest = new SearchRequest(INDEX_CONSUMER);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(queryBuilder);
        searchRequest.source(builder);

        return searchEsQueryResponse(searchRequest)
                .thenApply(this::getConsumerHits)
                .exceptionally(throwable -> {
                    throw new ElasticSearchException(throwable.getMessage());
                });
    }

    private List<ConsumerResponse>getConsumerHits(SearchResponse response){
        return Arrays.stream(response.getHits().getHits())
                .map(this::convertSearchToConsumer)
                .collect(Collectors.toList());
    }
    private ConsumerResponse convertSearchToConsumer(SearchHit hit) {
        Map<String, Object> map = hit.getSourceAsMap();
//        return gson.fromJson(gson.toJson(map),School.class);

        return new ConsumerResponse().builder()
                .userId(String.valueOf(map.get(USERID)))
                .firstName((String) map.get(FIRST_NAME))
                .lastName((String) map.get(LAST_NAME))
                .email((String) map.get(EMAIL))
                .mobileNumber((String) map.get(MOBILE_NUMBER))
                .state((String) map.get(STATE))
                .city((String) map.get(CITY))
                .address((String) map.get(ADDRESS))
                .tenantId(String.valueOf(map.get(TENANT_ID)))
                .build();
    }
}
