package web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.dao.UserDao;
import web.model.Role;

import java.util.HashSet;
import java.util.Set;

@Component
public class UtilServiceImpl implements UtilService {

    private UserDao userDao;

    @Autowired
    public UtilServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Set<Role> getRoleForUser(String role) {
        Set<Role> roles = new HashSet<>();
        try {
            String[] splitRoles = role.split(",");
            roles.add(userDao.getRoleByName(splitRoles[1]));
            roles.add(userDao.getRoleByName(splitRoles[0]));
            return roles;
        } catch (Exception e) {

        }
        roles.add(userDao.getRoleByName(role));
        return roles;
    }
}
