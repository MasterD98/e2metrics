package org.rapid.userservice.service.impl;

import jakarta.transaction.Transactional;
import org.rapid.userservice.entity.User;
import org.rapid.userservice.repository.UserRepository;
import org.rapid.userservice.repository.view.AccessTokenView;
import org.rapid.userservice.service.UserServiceIf;
import org.rapid.userservice.service.rqrs.user.*;
import org.rapid.userservice.service.rqrs.user.http.ChangeRoleHttpRq;
import org.rapid.userservice.service.rqrs.user.http.ChangeRoleHttpRs;
import org.rapid.userservice.service.rqrs.user.http.GetAccessTokenHttpRq;
import org.rapid.userservice.service.rqrs.user.http.GetAccessTokenHttpRs;
import org.rapid.userservice.util.EncryptorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserServiceIf {
    @Value("${github.client-id}")
    String clientId;
    @Value("${github.client-secret}")
    String clientSecret;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final EncryptorUtil encryptorUtil;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate, EncryptorUtil encryptorUtil) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.encryptorUtil = encryptorUtil;
    }

    @Override
    public GetAllUsersRs getAllUsers() {
        return new GetAllUsersRs(userRepository.findAll());
    }

    @Override
    public GetUserRs getUser(GetUserRq rq) {
        Optional<User> user = userRepository.getUserByUsername(rq.username());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("invalid username");
        }
        return new GetUserRs(user.get());
    }

    @Override
    public SaveUserRs saveUser(SaveUserRq rq) {
        return new SaveUserRs(userRepository
                .save(User.builder()
                        .email(rq.email())
                        .username(rq.username())
                        .firstName(rq.firstName())
                        .lastName(rq.lastName())
                        .build()
                )
        );
    }

    @Transactional
    @Override
    public DeleteUserRs deleteUser(DeleteUserRq rq) {
        int deletedRowCount = userRepository.deleteByUsername(rq.username());
        return new DeleteUserRs(deletedRowCount > 0);
    }

    @Override
    public SaveGithubTokenRs saveGithubToken(SaveGithubTokenRq rq) {

        GetAccessTokenHttpRq requestBody = GetAccessTokenHttpRq.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .code(rq.code())
                .build();

        ResponseEntity<GetAccessTokenHttpRs> response = restTemplate.postForEntity("https://github.com/login/oauth/access_token", requestBody, GetAccessTokenHttpRs.class);

        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException("request to github failed");
        }

        Optional<User> existingUser = userRepository.getUserByUsername(rq.username());

        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("invalid username");
        }

        String accessToken = Objects.requireNonNull(response.getBody()).getAccess_token();

        User updateUser = existingUser.get();
        updateUser.setGithubToken(encryptorUtil.encrypt(accessToken));
        userRepository.save(updateUser);
        return new SaveGithubTokenRs(accessToken);
    }

    @Override
    public GetGithubTokenRs getGithubToken(GetGithubTokenRq rq) {
        Optional<AccessTokenView> accessToken = userRepository.getGithubToken(rq.username());
        if (accessToken.isEmpty()) {
            return new GetGithubTokenRs("");
        }
        String decryptedAccessToken = encryptorUtil.decrypt(accessToken.get().getGithubToken());

        return new GetGithubTokenRs(decryptedAccessToken);
    }

    @Override
    public ChangeRoleRs changeRole(ChangeRoleRq rq) {
        ResponseEntity<ChangeRoleHttpRs> rs = restTemplate.postForEntity("http://auth-service/auth/role", new ChangeRoleHttpRq(rq.username(), rq.role()), ChangeRoleHttpRs.class);

        if (rs.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new RuntimeException();
        }

        Optional<User> optionalUser = userRepository.getUserByUsername(rq.username());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("invalid username");
        }

        User user = optionalUser.get();
        user.setRole(rq.role());

        user = userRepository.save(user);

        return new ChangeRoleRs(user);
    }

    @Override
    public UpdateUserRs updateUser(UpdateUserRq rq) {
        Optional<User> optionalUser = userRepository.getUserByUsername(rq.username());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("invalid username");
        }
        User user = optionalUser.get();

        if (!rq.email().isEmpty()) {
            user.setEmail(rq.email());
        }

        if (!rq.firstName().isEmpty()) {
            user.setFirstName(rq.firstName());
        }

        if (!rq.lastName().isEmpty()) {
            user.setLastName(rq.lastName());
        }

        if (!rq.comparisonLayout().isEmpty()) {
            user.setComparisonLayout(rq.comparisonLayout());
        }

        if (!rq.forecastLayout().isEmpty()) {
            user.setForecastLayout(rq.forecastLayout());
        }

        if (!rq.overviewLayout().isEmpty()) {
            user.setOverviewLayout(rq.overviewLayout());
        }

        if (rq.profilePicture().length != 0) {
            user.setProfilePicture(rq.profilePicture());
        }

        user = userRepository.save(user);

        return new UpdateUserRs(user);
    }

    @Override
    public ChangeReportStatusRs changeReportStatus(ChangeReportStatusRq rq) {
        Optional<User> optionalUser = userRepository.getUserByUsername(rq.username());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("invalid username");
        }
        User user = optionalUser.get();

        user.setReportsEnable(rq.status());
        user = userRepository.save(user);
        return new ChangeReportStatusRs(user.isReportsEnable());
    }

}
