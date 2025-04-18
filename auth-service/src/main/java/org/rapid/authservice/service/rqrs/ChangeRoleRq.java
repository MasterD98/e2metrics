package org.rapid.authservice.service.rqrs;

import org.rapid.authservice.model.Role;

public record ChangeRoleRq(String username, Role role) {
}
