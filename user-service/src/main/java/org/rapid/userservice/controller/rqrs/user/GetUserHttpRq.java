package org.rapid.userservice.controller.rqrs.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;

public record GetUserHttpRq (@NotNull @PathVariable String username){
}
