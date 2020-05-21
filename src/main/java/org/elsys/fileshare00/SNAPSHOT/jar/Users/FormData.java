package org.elsys.fileshare00.SNAPSHOT.jar.Users;

import lombok.Data;

@Data
public class FormData {
    public String username;
    public String password;
    public String repeat_password;
    public String email;
}
