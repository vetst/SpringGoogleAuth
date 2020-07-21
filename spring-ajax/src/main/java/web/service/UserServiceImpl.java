package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.dao.UserRepository;
import web.model.User;
import web.util.UtilService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UtilService utilService;
    private UserDao userDao;
    private final UserRepository userRepository;

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public UserServiceImpl(UserDao userDao, UserRepository userRepository) {
        this.userDao = userDao;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public boolean addUser(User user, String roles) {
        if (user.getFirstName() != null && user.getLastName() != null && user.getPassword() != null
                && user.getEmail() != null && user.getAge() != 0) {
            user.setRoles(utilService.getRoleForUser(roles));
            userDao.addUser(user);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateUser(User user, String roles) {
        if (user.getId() != null && user.getFirstName() != null && user.getLastName() != null && user.getPassword() != null
                && user.getEmail() != null && user.getAge() != 0 && roles != null) {

            user.setRoles(utilService.getRoleForUser(roles));
            userDao.updateUser(user);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteUser(Long id) {
        if (id != null) {
            userDao.deleteUser(id);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }


    @Override
    public String ifPasswordNull(Long id, String password) {
        if (password.trim().length() == 0) {
            return userDao.getUserById(id).getPassword();
        } else {
            return password;
        }
    }

    @Override
    public boolean isNotReg(String email) {
        return userDao.isNotReg(email);
    }

    @Override
    public User getUserByName(String email) {
        return userDao.getUserByName(email);
    }

    @Override
    public Optional<User> getUserByGoogleId(String id) {
        return Optional.ofNullable(userRepository.findUserByGoogleID(id));
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return Optional.ofNullable(userRepository.findUserByLogin(login));
    }
}
