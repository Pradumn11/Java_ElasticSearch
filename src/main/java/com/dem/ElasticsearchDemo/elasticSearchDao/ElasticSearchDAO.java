package com.dem.ElasticsearchDemo.elasticSearchDao;


import com.dem.ElasticsearchDemo.Config.Listener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class ElasticSearchDAO {

    private final RestHighLevelClient esClient;


    @Autowired
    public ElasticSearchDAO(RestHighLevelClient esClient) {
        this.esClient = esClient;
    }

    public CompletionStage<SearchResponse>searchEsQueryResponse(SearchRequest request){
        CompletableFuture<SearchResponse>completableFuture=new CompletableFuture<>();
        esClient.searchAsync(request,RequestOptions.DEFAULT, new Listener(completableFuture));
      return completableFuture;
    }
    public CompletionStage<GetResponse>getEsQueryResponse(GetRequest request){
        CompletableFuture<GetResponse>completableFuture=new CompletableFuture<>();

        esClient.getAsync(request, RequestOptions.DEFAULT, new Listener(completableFuture));
        return completableFuture;
    }

    public CompletionStage<DeleteResponse>deleteDocument(String index, String type,String id){

        DeleteRequest request=new DeleteRequest(index,type,id);
        CompletableFuture<DeleteResponse>completableFuture=new CompletableFuture<>();
        esClient.deleteAsync(request,RequestOptions.DEFAULT,new Listener(completableFuture));
        return completableFuture;
    }
    public CompletionStage<IndexResponse>addDocumentToIndex(String index, String id, Map<String,String> json){
        IndexRequest request=new IndexRequest(index).id(id).source(json);
        CompletableFuture<IndexResponse>completableFuture=new CompletableFuture<>();
        esClient.indexAsync(request,RequestOptions.DEFAULT,new Listener(completableFuture));
        return completableFuture;
    }
    public CompletionStage<UpdateResponse>updateDocument(UpdateRequest request){
        CompletableFuture<UpdateResponse>future=new CompletableFuture<>();
        esClient.updateAsync(request,RequestOptions.DEFAULT,new Listener(future));
        return future;
    }
    public CompletionStage<IndexResponse>addDocumentToIndex(String index,String id,String json){
        IndexRequest request=new IndexRequest(index).id(id).source(json, XContentType.JSON);
        CompletableFuture<IndexResponse>completableFuture=new CompletableFuture<>();
        esClient.indexAsync(request,getHeaders(),new Listener(completableFuture));
        return completableFuture;
    }

    public CompletionStage<ActionResponse>getHealth(){
        CompletableFuture<ActionResponse>future=new CompletableFuture<>();
        esClient.cluster().healthAsync(new ClusterHealthRequest(),RequestOptions.DEFAULT,new Listener(future));
        return future;
    }
    private RequestOptions getHeaders(){
        RequestOptions.Builder options = RequestOptions.DEFAULT.toBuilder();
        options.addHeader(HttpHeaders.ACCEPT,"application/vnd.elasticsearch+json;compatible-with=7");
        options.addHeader(HttpHeaders.CONTENT_TYPE,"application/vnd.elasticsearch+json;compatible-with=7");
        return options.build();
    }
}
