package com.dem.ElasticsearchDemo.school.dao;

import com.dem.ElasticsearchDemo.Exception.ElasticSearchException;
import com.dem.ElasticsearchDemo.Model.School;
import com.dem.ElasticsearchDemo.elasticSearchDao.EsAbstractDao;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Repository
public class SchoolEsDao extends EsAbstractDao {

    private final String SCHOOL_IN = "school";
    private final String ID = "id";
    private final String AGE = "age";
    private final String CITY = "city";
    private final String FULLNAME = "fullName";
    private final String CONTACT = "contact";
    private final String ATTRIBUTES = "attributes";
    private final String ATTRIBUTES_ADHAR = "attributes.adharcard";
    private final String ATTRIBUTES_PANCARD = "attributes.pancard";
    private final String TYPE = "type";

    private Gson gson;

    public SchoolEsDao(@Qualifier("demo.restClient") RestHighLevelClient esClient) {
        super(esClient);
        this.gson = new Gson();
    }

    public CompletionStage<List<School>> searchSchoolByType(String type) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        QueryBuilder query = QueryBuilders.termQuery(TYPE, type);
        queryBuilder.must(query);
        SearchRequest searchRequest = new SearchRequest(SCHOOL_IN);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(10)
                .from(0);
        searchRequest.source(searchSourceBuilder);
        return searchEsQueryResponse(searchRequest)
                .thenApply(this::getSchoolHits)
                .exceptionally(throwable -> {
                    throw new ElasticSearchException(throwable.getMessage());
                });
    }

    public CompletionStage<List<School>>getByAdharOrPanCard(Long adharcard, Long panCard) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.should(QueryBuilders.termQuery(ATTRIBUTES_ADHAR, adharcard))
                .should(QueryBuilders.termQuery(ATTRIBUTES_PANCARD, panCard));
//                .minimumShouldMatch(1);
        //OR
        BoolQueryBuilder filter = new BoolQueryBuilder();
        filter.must(queryBuilder);

        SearchRequest searchRequest = new SearchRequest(SCHOOL_IN);
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(filter);
        searchRequest.source(builder);
        return searchEsQueryResponse(searchRequest)
                .thenApply(this::getSchoolHits)
                .exceptionally(throwable -> {
                    throw new ElasticSearchException(throwable.getMessage());
                });
    }

    private List<School> getSchoolHits(SearchResponse response) {

        return Arrays.stream(response.getHits().getHits())
                .map(this::convertSearchToSchool)
                .collect(Collectors.toList());
    }

    private School convertSearchToSchool(SearchHit hit) {
        Map<String, Object> map = hit.getSourceAsMap();
//        return gson.fromJson(gson.toJson(map),School.class);
        System.out.println(gson.toJson(map));
        return new School().builder()
                .id(((Number) map.get(ID)).longValue())
                .age(((Number) (map.get(AGE))).longValue())
                .city((String) map.get(CITY))
                .contact(((Number) (map.get(CONTACT))).longValue())
                .fullName((String) map.get(FULLNAME))
                .type((String) map.get(TYPE))
                .attributes((Map<String, Object>) map.get(ATTRIBUTES))
                .build();
    }

}
