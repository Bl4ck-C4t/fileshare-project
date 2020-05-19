package org.elsys.fileshare00.SNAPSHOT.jar;

import org.elsys.fileshare00.SNAPSHOT.jar.Users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@RestController
public class HelloController {

    @Autowired
    public UsersRepo usersRepo;

    @Autowired
    public AuthorityRepo authorityRepo;

    private EmailServiceImpl emailService = new EmailServiceImpl();

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
        if(principal == null){
            return null;
        }
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
        unregistered_user.activationCode = genRandomString(15);
        Authority auth = new Authority();
        auth.username = unregistered_user.username;
        auth.authority = "USER";

        final String link = "http://localhost:8080/activate?code=" + unregistered_user.activationCode;
        String mes = "Click this link to activate your account: " + link;
        emailService.sendSimpleMessage(unregistered_user.email, "Activate your Fileserver account", mes);
        boolean Wascreated = new File("./UsersFiles/" + unregistered_user.username).mkdir();
        if(!Wascreated){
            System.out.println("Couldn't create user dir for " + unregistered_user.username);
        }
        // usersRepo.save(unregistered_user);
        // authorityRepo.save(auth);
        return "It's good";
    }

    @GetMapping("/api/activate")
    public boolean activateAccount(@RequestParam("code") String activationCode){
        User user = usersRepo.findByActivationCode(activationCode);
        if (user == null){
            return false;
        }
        user.activate();
        usersRepo.save(user);
        return true;
    }

    @GetMapping("/api/getFiles")
    public JsonFile getFiles(@RequestParam String path, Principal user) throws IOException {
        if (user == null){
            return null;
        }
        path = path.replace("../", "");
        path = path.replace("..", "");
        path = String.format("./UsersFiles/%s/%s", user.getName(), path);
        File file = new File(path);
        if (file.isDirectory()){
            return new JsonFile(true, Arrays.asList(Objects.requireNonNull(file.list())), null,
                    file.getName());
        }
        else{

            String content = Files.readString(Paths.get(path), StandardCharsets.US_ASCII);
            return new JsonFile(false, null, content, file.getName());

        }
    }

    @PostMapping("/api/uploadFile")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {

        return new ResponseEntity(HttpStatus.ACCEPTED);
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