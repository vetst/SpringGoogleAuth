package web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;

import java.util.HashSet;
import java.util.Set;

@Component
public class UtilServiceImpl implements UtilService {

    private RoleDao roleDao;

    @Autowired
    public UtilServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Set<Role> getRoleForUser(String role) {
        Set<Role> roles = new HashSet<>();
        try {
            String[] splitRoles = role.split(",");
            roles.add(roleDao.getRoleByName(splitRoles[1]));
            roles.add(roleDao.getRoleByName(splitRoles[0]));
            return roles;
        } catch (Exception e) {

        }
        roles.add(roleDao.getRoleByName(role));
        return roles;
    }
}
