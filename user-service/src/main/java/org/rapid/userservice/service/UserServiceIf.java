package org.rapid.userservice.service;

import org.rapid.userservice.service.rqrs.user.*;

public interface UserServiceIf {
    GetAllUsersRs getAllUsers();
    GetUserRs getUser(GetUserRq rq);
    SaveUserRs saveUser(SaveUserRq rq);
    DeleteUserRs deleteUser(DeleteUserRq rq);
    SaveGithubTokenRs saveGithubToken(SaveGithubTokenRq rq);
    GetGithubTokenRs getGithubToken(GetGithubTokenRq rq);
    ChangeRoleRs changeRole(ChangeRoleRq rq);
    UpdateUserRs updateUser(UpdateUserRq rq);
    ChangeReportStatusRs changeReportStatus(ChangeReportStatusRq rq);
}
