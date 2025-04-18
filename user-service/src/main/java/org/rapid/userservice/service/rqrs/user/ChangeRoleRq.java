package org.rapid.userservice.service.rqrs.user;

import org.rapid.userservice.model.Role;

public record ChangeRoleRq(String username, Role role) {
}
