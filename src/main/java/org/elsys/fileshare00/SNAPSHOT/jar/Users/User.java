package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    public int id;

    @Column(unique = true)
    public String username;

    @Column(length = 60)
    public String password;

    @Column
    public String email;

    @Column
    public boolean enabled;

    @Column(name = "activation_code", length = 60, unique = true)
    public String activationCode;

    public void activate() {
        enabled = true;
    }
}
