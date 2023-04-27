package com.dem.ElasticsearchDemo.Model;


import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School implements Serializable {

    Long id;
    String type;
    String fullName;
    Long age;
    String city;
    Long contact;
    Map<String, Object> attributes;

}


