package org.rapid.userservice.service.rqrs.user.http;

import org.rapid.userservice.model.Role;

public record ChangeRoleHttpRq(String username, Role role) {
}
