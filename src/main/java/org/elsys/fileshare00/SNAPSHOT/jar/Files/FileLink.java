package org.elsys.fileshare00.SNAPSHOT.jar.Files;

import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FileLink {
    @Id
    @GeneratedValue
    public int id;

    @Column
    public String path;

}
