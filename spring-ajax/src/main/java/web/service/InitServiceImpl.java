package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class InitServiceImpl implements InitService {

    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public InitServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void addAdminAndUser() {
        if (!userDao.isNotReg("admin@mail.com")) {
            roleDao.addRole(new Role("ADMIN"));
            roleDao.addRole(new Role("USER"));
            Set<Role> admin = new HashSet<>();
            admin.add(roleDao.getRoleByName("ADMIN"));
            admin.add(roleDao.getRoleByName("USER"));
            userDao.addUser(new User("Иван", "Иванов", "admin", "admin@mail.com", 35, admin));

            Set<Role> user = new HashSet<>();
            user.add(roleDao.getRoleByName("USER"));
            userDao.addUser(new User("Петр", "Петров", "user", "user@mail.com", 25, user));
        }
    }
}
