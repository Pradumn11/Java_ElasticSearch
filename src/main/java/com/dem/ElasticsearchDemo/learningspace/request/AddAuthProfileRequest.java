package com.dem.ElasticsearchDemo.learningspace.request;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddAuthProfileRequest {

    private String userId;
    private String mobileNumber;
    private String email;
    private Long tenantId;
    private String userName;
    private String password;
    private String role;
}
