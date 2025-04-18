package org.rapid.authservice.controller.rqrs;

import jakarta.validation.constraints.NotNull;

public record LoginHttpRq (@NotNull(message = "username cannot be null") String username,
                           @NotNull(message = "password cannot be null") String password){
}
