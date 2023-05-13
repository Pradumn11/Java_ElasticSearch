package com.dem.ElasticsearchDemo.learningspace.request;


import lombok.*;
import org.elasticsearch.common.Strings;

import java.util.Map;

import static com.dem.ElasticsearchDemo.utils.ObjectUtils.ValidateData;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllConsumerRequest {

    Map<String,Object>fields;
    Integer pageNumber;
    Integer pageSize;
    Long tenantId;

    public void validate(){
        ValidateData(tenantId!=null,"tenantId should be Present");
    }
}
