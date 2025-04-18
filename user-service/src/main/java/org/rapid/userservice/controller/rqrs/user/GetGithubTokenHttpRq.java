package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public record GetGithubTokenHttpRq (@NotNull @RequestParam String username){
}
