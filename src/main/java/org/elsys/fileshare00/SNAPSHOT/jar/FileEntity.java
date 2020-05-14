package org.elsys.fileshare00.SNAPSHOT.jar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileEntity {

    @Id
    public int id;

    @Column
    public String name;


@Column
    public int length;
}
