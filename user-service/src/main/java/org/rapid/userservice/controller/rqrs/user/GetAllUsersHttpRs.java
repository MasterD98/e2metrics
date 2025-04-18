package org.rapid.userservice.controller.rqrs.user;

import org.rapid.userservice.entity.User;

import java.util.List;

public record GetAllUsersHttpRs (List<User> users){
}
