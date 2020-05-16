package org.elsys.fileshare00.SNAPSHOT.jar;

import org.elsys.fileshare00.SNAPSHOT.jar.Users.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static String genRandomString(int targetStringLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        return random.ints(0, characters.length())
                .limit(targetStringLength)
                .collect(StringBuilder::new, (x,y) -> x.append(characters.charAt(y)), StringBuilder::append)
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
        unregistered_user.activationCode = genRandomString(10);
        Authority auth = new Authority();
        auth.username = unregistered_user.username;
        auth.authority = "USER";

        // usersRepo.save(unregistered_user);
        // authorityRepo.save(auth);


        return "It's good";
    }

    @GetMapping("/api/sendMessage")
    public void testSendMessage(){
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage("stef4oben88@gmail.com", "Testing", "Test completed");
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
        String randomCode = HelloController.genRandomString(20);
        // register code in database
        return "/api/getFiles?access_code="+randomCode;
    }
//    public String getCurrentUserName(Principal principal){
//        return principal.getName();
//    }
}