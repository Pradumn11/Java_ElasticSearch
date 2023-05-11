package com.dem.ElasticsearchDemo.learningspace.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerPreviewResponse {

    List<ConsumerResponse>consumers;
    Long totalCount;
}
