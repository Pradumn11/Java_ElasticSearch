package com.dem.ElasticsearchDemo.learningspace.response;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthProfileResponse {

    private Long userId;
    private String mobileNumber;
    private String email;
    private String tenantId;
    private String status;

}
