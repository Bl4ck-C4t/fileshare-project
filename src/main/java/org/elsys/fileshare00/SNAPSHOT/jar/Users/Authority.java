package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue
    public int id;

    @Column
    public String username;

    @Column
    public String authority;
}
