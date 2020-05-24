package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Qualifier("UserRepo")
public interface UsersRepo extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.username= ?1")
    public User findByUsername(String username);

    @Query("select u from User u where u.activationCode = ?1")
    public User findByActivationCode(String activationCode);
}
