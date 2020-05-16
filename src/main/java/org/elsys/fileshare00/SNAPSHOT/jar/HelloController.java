package org.elsys.fileshare00.SNAPSHOT.jar;

import org.elsys.fileshare00.SNAPSHOT.jar.Users.Authority;
import org.elsys.fileshare00.SNAPSHOT.jar.Users.AuthorityRepo;
import org.elsys.fileshare00.SNAPSHOT.jar.Users.User;
import org.elsys.fileshare00.SNAPSHOT.jar.Users.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@RestController
public class HelloController {

    @Autowired
    public UsersRepo usersRepo;

    @Autowired
    public AuthorityRepo authorityRepo;

    private static String genRandomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @GetMapping("/api/getUser")
    public String getCurrentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/api/userExists")
    public boolean userExists(@RequestParam("username") String username){
        User foundUser = usersRepo.findByUsername(username);
        return foundUser != null;
    }

    @PostMapping("/api/register")
    @ResponseBody
    public String ProcessRegister(FormData body){
        User unregistered_user = new User();
        unregistered_user.email = body.email;
        unregistered_user.username = body.username;
        unregistered_user.password = body.password;
        unregistered_user.enabled = true;
        Authority auth = new Authority();
        auth.username = unregistered_user.username;
        auth.authority = "USER";

        usersRepo.save(unregistered_user);
        authorityRepo.save(auth);
        return "It's good";
    }


    @GetMapping("/api/getFiles")
    public List<String> getFiles(@RequestParam String path){
        List<String> files = new ArrayList<String>();
        files.add("file1.txt");
        // get files and directories at a path

        return files;
    }

    @GetMapping("/api/getFilesWithLink")
    public List<String> getFilesWithLink(@RequestParam String code){
        // check if code is valid
        List<String> files = new ArrayList<String>();
        files.add("file1.txt");
        // get files and directories at a path

        return files;
    }

    @PutMapping("/api/putFile")
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path){
        //upload file to path
    }

    @GetMapping("/api/generateLink")
    public String generateProtectedLink(@RequestParam("path") String filepath){
        String randomCode = HelloController.genRandomString();
        // register code in database
        return "/api/getFiles?access_code="+randomCode;
    }
//    public String getCurrentUserName(Principal principal){
//        return principal.getName();
//    }
}