package web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("from User u where u.googleID = :googleID")
    User findUserByGoogleID(@Param("googleID") String googleID);

    @Query("from User u where u.login = :login")
    User findUserByLogin(@Param("login") String login);

}
