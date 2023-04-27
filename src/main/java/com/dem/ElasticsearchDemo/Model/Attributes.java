package com.dem.ElasticsearchDemo.Model;

import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attributes {

    Long adharcard;
    Long pancard;
}
