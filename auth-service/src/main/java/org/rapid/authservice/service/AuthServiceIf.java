package org.rapid.authservice.service;

import org.rapid.authservice.service.rqrs.*;

public interface AuthServiceIf {
    LoginRs login(LoginRq loginRq);
    RegisterRs register(RegisterRq registerRq);
    ChangeRoleRs changeRole(ChangeRoleRq changeRoleRq);
}
