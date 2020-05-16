package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    public int id;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String email;

    @Column
    public boolean enabled;
}
