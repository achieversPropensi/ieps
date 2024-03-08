package achievers.ieps.backend.service;

import achievers.ieps.backend.model.Role;
import achievers.ieps.backend.repository.RoleDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDb roleDb;

    @Override
    public List<Role> getAllList() { return roleDb.findAll(); }

    @Override
    public Role getRoleByRoleName(String name){
        Optional<Role> role = roleDb.findByRole(name);
        if (role.isPresent()){
            return role.get();
        }
        return null;
    }
}

