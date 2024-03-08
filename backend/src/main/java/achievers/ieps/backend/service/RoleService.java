package achievers.ieps.backend.service;

import achievers.ieps.backend.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllList();
    Role getRoleByRoleName(String name);

}
