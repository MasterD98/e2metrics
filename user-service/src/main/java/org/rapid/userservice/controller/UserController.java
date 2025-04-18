package org.rapid.userservice.controller;

import jakarta.validation.Valid;
import org.rapid.userservice.controller.rqrs.user.*;
import org.rapid.userservice.service.UserServiceIf;
import org.rapid.userservice.service.rqrs.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserServiceIf userService;

    @Autowired
    public UserController(UserServiceIf userService) {
        this.userService = userService;
    }

    @GetMapping("")
    ResponseEntity<GetAllUsersHttpRs> getAllUsers() {
        return ResponseEntity.ok(new GetAllUsersHttpRs(userService.getAllUsers().users()));
    }

    @GetMapping("{username}")
    ResponseEntity<GetUserHttpRs> getUser(@Valid GetUserHttpRq rq) {
        GetUserRs rs = userService.getUser(new GetUserRq(
                rq.username()
        ));
        return ResponseEntity.ok(new GetUserHttpRs(rs.user()));
    }

    @DeleteMapping("{username}")
    ResponseEntity<DeleteUserHttpRs> deleteUser(@Valid DeleteUserHttpRq rq) {
        DeleteUserRs rs = userService.deleteUser(new DeleteUserRq(rq.username()));
        return ResponseEntity.ok(new DeleteUserHttpRs(rs.success()));
    }

    @PostMapping("save")
    ResponseEntity<SaveUserHttpRs> saveUser(@Valid @RequestBody SaveUserHttpRq rq) {
        SaveUserRs rs = userService.saveUser(SaveUserRq.builder()
                .email(rq.email())
                .firstName(rq.firstName())
                .lastName(rq.lastName())
                .username(rq.username())
                .role(rq.role())
                .build()
        );
        return ResponseEntity.ok(new SaveUserHttpRs(rs.user()));
    }

    @GetMapping("/githubToken")
    ResponseEntity<GetGithubTokenHttpRs> getGithubToken(@Valid GetGithubTokenHttpRq rq) {
        GetGithubTokenRs rs = userService.getGithubToken(new GetGithubTokenRq(rq.username()));
        return ResponseEntity.ok(new GetGithubTokenHttpRs(rs.accessToken()));
    }

    @PostMapping("/githubToken")
    ResponseEntity<SaveGithubTokenHttpRs> saveGithubToken(@Valid @RequestBody SaveGithubTokenRq rq){
        SaveGithubTokenRs rs = userService.saveGithubToken(new SaveGithubTokenRq(rq.code(),rq.username()));
        return ResponseEntity.ok(new SaveGithubTokenHttpRs(rs.accessToken()));
    }

    @PutMapping("role")
    ResponseEntity<ChangeRoleHttpRs> changeRole(@Valid @RequestBody ChangeRoleHttpRq rq ){
        ChangeRoleRs rs = userService.changeRole(new ChangeRoleRq(rq.username(),rq.role()));
        return ResponseEntity.ok(new ChangeRoleHttpRs(rs.user()));
    }

    @PutMapping("/report/status")
    ResponseEntity<ChangeReportStatusHttpRs> changeReportStatus(@Valid @RequestBody ChangeReportStatusHttpRq rq){
        ChangeReportStatusRs rs = userService.changeReportStatus(new ChangeReportStatusRq(rq.username(),rq.status()));
        return ResponseEntity.ok(new ChangeReportStatusHttpRs(rs.status()));
    }
    @PutMapping("update")
    ResponseEntity<UpdateUserHttpRs> updateUser(@Valid @RequestBody UpdateUserRq rq){
        UpdateUserRs rs = userService.updateUser(UpdateUserRq.builder()
                        .comparisonLayout(rq.comparisonLayout())
                        .email(rq.email())
                        .forecastLayout(rq.forecastLayout())
                        .firstName(rq.firstName())
                        .lastName(rq.lastName())
                        .overviewLayout(rq.overviewLayout())
                        .profilePicture(rq.profilePicture())
                        .username(rq.username())
                .build()
        );
        return ResponseEntity.ok(new UpdateUserHttpRs(rs.user()));
    }
}
