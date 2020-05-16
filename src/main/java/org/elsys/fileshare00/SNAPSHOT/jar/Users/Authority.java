package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    public int id;

    @Column
    public String username;

    @Column
    public String authority;
}
