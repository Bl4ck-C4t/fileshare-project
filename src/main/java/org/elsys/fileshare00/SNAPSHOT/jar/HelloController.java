package org.elsys.fileshare00.SNAPSHOT.jar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class HelloController {

    @Autowired
    public FileRepo fileRep;

    @GetMapping("/api/hello")

    public String hello(@RequestParam int id) {

        Optional<FileEntity> opt = fileRep.findById(id);
        if (opt.isPresent()) {
            return opt.get().name;
        }
        else{
            return String.format("No such entry with id = %d", id);
        }

    }
//
//    @GetMapping("/api/user")
//    public String getCurrentUserName(Principal principal){
//        return principal.getName();
//    }
}