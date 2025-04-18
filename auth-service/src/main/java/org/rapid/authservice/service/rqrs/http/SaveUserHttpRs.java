package org.rapid.authservice.service.rqrs.http;

import lombok.*;

@Getter
@Setter
public class SaveUserHttpRs {
    private User user;
}

@Getter
@Setter
class User{
    private long id;
    private String username;
    private String email;
    private String subscription;
    private String firstName;
    private String lastName;
    private String overviewLayout;
    private String comparisonLayout;
    private String forecastLayout;
    private byte[] githubToken;
    private byte[] profilePicture;
    private boolean isReportsEnable;
}
