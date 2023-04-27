package com.dem.ElasticsearchDemo.Config;


import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.transport.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


@Configuration
public class ElasticSearchConfig {

    @Bean(name = "demo.restClient")
    public RestHighLevelClient client(@Value("${elasticsearch.host}")String clusterHost,
                                      @Value("${elasticsearch.port}")Integer port){


        RestHighLevelClient restHighLevelClient= new RestHighLevelClient(
                RestClient.builder(new HttpHost(clusterHost,port))

        );
        return restHighLevelClient;
    }






}
