package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Qualifier("UserRepo")
public interface UsersRepo extends JpaRepository<User, Integer> {
    @Query(value = "select * from users u where u.username= ?1", nativeQuery = true)
    public User findByUsername(String username);
}
