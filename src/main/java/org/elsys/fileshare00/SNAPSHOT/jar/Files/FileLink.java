package org.elsys.fileshare00.SNAPSHOT.jar.Files;

import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FileLink {
    @Id
    @GeneratedValue
    public int id;

    @Column(length = 60, unique = true)
    public String code;

    @Column
    @NotNull
    public String path;

}
