package com.dem.ElasticsearchDemo.employee.dao;

import com.dem.ElasticsearchDemo.Exception.ElasticSearchException;
import com.dem.ElasticsearchDemo.Model.Employee;
import com.dem.ElasticsearchDemo.elasticSearchDao.EsAbstractDao;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Service
public class EmployeeElasticSearchDao extends EsAbstractDao {


    private final String searchTimeout;
    private final String INDEX_EMPLOYEE="employee";
    private final Integer defaultPageSize;
    private final Gson gson;
    @Autowired
    public EmployeeElasticSearchDao(@Qualifier("demo.restClient") RestHighLevelClient client) {
        super(client);
       this.searchTimeout= "300s";
       this.defaultPageSize=10;
       this.gson=new Gson();
    }

    public CompletionStage<List<Employee>>searchEmployees(Integer id){
        BoolQueryBuilder queryBuilder=new BoolQueryBuilder();


/*
        try
QueryBuilder builder= QueryBuilders.matchQuery("surName","Zendphale");
        QueryBuilder builder1=QueryBuilders.matchPhrasePrefixQuery("surName","Zen");
*/
        QueryBuilder builder2=QueryBuilders.regexpQuery("firstName",".*t.*");//match any word consisting t
        if (id!=null){
            queryBuilder.must(addIdFilter(id));
        }
        SearchRequest searchRequest=new SearchRequest(INDEX_EMPLOYEE);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder()
                .from(0)
                .size(10)
                .timeout(TimeValue.parseTimeValue(searchTimeout,"timeout"))
                .query(builder2);
        searchRequest.source(searchSourceBuilder);
        return searchEsQueryResponse(searchRequest)
                .thenApply(searchResponse -> getEmployeeHits(searchResponse))
                .exceptionally(throwable ->
                        {
                          throw new ElasticSearchException(throwable.getMessage());
                        }
                        );
    }
    private TermQueryBuilder addIdFilter(Integer id){
        return new TermQueryBuilder("id",id);
    }
    private List<Employee>getEmployeeHits(SearchResponse response){
        System.out.println(response.getHits().getHits().length);
        return Arrays.stream(response.getHits().getHits())
                .map(searchhit->convertSearchToEmployee(searchhit))
                .collect(Collectors.toList());
    }
    private  Employee convertSearchToEmployee(SearchHit searchHit){
        Map<String, Object>objectMap=searchHit.getSourceAsMap();
        return new Employee().builder()
                .id((Integer) objectMap.get("id"))
                .firstName((String)objectMap.get("firstName"))
                .surName((String) objectMap.get("surName"))
                .address((String) objectMap.get("address"))
                .salary((Integer) objectMap.get("salary"))
                .build();
    }

    public CompletionStage<Void>addEmployee(Employee employee){

        String id="employee"+"__"+employee.getId();
        String json=gson.toJson(employee);
       return addDocumentToIndex(INDEX_EMPLOYEE,id,json)
               .thenAccept(indexResponse -> System.out.println("Index: "+indexResponse.getResult()));
    }
}
