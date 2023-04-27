package com.dem.ElasticsearchDemo.Model;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    Integer id;
    String firstName;
    String surName;
    String address;
    Integer salary;

}
