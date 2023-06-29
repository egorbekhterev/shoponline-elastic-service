package com.shoponline.elastic.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.shoponline.elastic.repository"})
public class ElasticConfiguration {

    @Bean
    public RestHighLevelClient elasticClient() {
        String elasticHost = System.getenv("ELASTIC_HOST");
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(elasticHost, 9200, "http")));
    }
}
