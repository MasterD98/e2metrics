package org.rapid.userservice.service.rqrs.user;

import org.rapid.userservice.entity.User;

import java.util.List;

public record GetAllUsersRs(List<User> users) {
}
