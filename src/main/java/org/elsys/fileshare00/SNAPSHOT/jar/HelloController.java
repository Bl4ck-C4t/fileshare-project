package org.elsys.fileshare00.SNAPSHOT.jar;

import org.elsys.fileshare00.SNAPSHOT.jar.Files.FileLink;
import org.elsys.fileshare00.SNAPSHOT.jar.Files.FileLinkRepo;
import org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses.FileInfo;
import org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses.FileJson;
import org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses.ServedFile;
import org.elsys.fileshare00.SNAPSHOT.jar.Users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @Autowired
    public UsersRepo usersRepo;

    @Autowired
    public AuthorityRepo authorityRepo;

    @Autowired
    public FileLinkRepo newFolder;

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

    private FileJson getJsonFileFromPath(String path){
        File file = new File(path);
        if (file.isDirectory()){
            List<FileInfo> fileObjects = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .map(fl -> new FileInfo(
                            fl.isDirectory(), fl.getName(),
                            newFolder.pathExists(file.getPath() + "/" + fl.getName())))
                    .collect(Collectors.toList());

            return new FileJson(null, fileObjects);
        }
        else{
            try {
                String content = Files.readString(Paths.get(path), StandardCharsets.US_ASCII);
                return new FileJson(new ServedFile(file.getName(), content), null);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private String validatePath(String path){
        path = path.replace("../", "");
        path = path.replace("..", "");

        return path;
    }

    private String pathToUserPath(String path, Principal user){
        path = validatePath(path);
        path = String.format("./UsersFiles/%s/%s", user.getName(), path);
        return path;
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
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        User unregistered_user = new User();
        unregistered_user.email = body.email;
        unregistered_user.username = body.username;
        unregistered_user.password = body.password;
        unregistered_user.enabled = true;
        Authority auth = new Authority();
        auth.username = unregistered_user.username;
        auth.authority = "USER";
        User user = usersRepo.save(unregistered_user);
        user.activationCode = bcrypt.encode(Integer.toString(user.id));
        usersRepo.save(user);
        authorityRepo.save(auth);

        final String link = "http://localhost:8080/activate?code=" + user.activationCode;
        String mes = "Click this link to activate your account: " + link;
        emailService.sendSimpleMessage(unregistered_user.email, "Activate your Fileserver account", mes);
        boolean Wascreated = new File("./UsersFiles/" + unregistered_user.username).mkdir();
        if(!Wascreated){
            System.out.println("Couldn't create user dir for " + unregistered_user.username);
        }

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
    public FileJson getFiles(@RequestParam String path, @RequestParam(defaultValue = "") String access_code,
                             Principal user) {
        if (user == null){
            return null;
        }

        path = pathToUserPath(path, user);

        return getJsonFileFromPath(path);
    }

    @PutMapping("/api/mkdir")
    public String createDirectory(@RequestParam String path, Principal user, HttpServletResponse response){
        path = pathToUserPath(path, user);
        File currentFolder = new File(path);
        File newFolder = new File(path + "/NewFolder");

        if(newFolder.exists()){
            int newFoldersCount = (int)Arrays.stream(Objects.requireNonNull(currentFolder.listFiles()))
                    .filter(fl -> fl.getName().startsWith("NewFolder")).count();

            newFolder = new File(path + "/NewFolder"+
                    (newFoldersCount > 0 ? "(" + newFoldersCount + ")" : ""));
        }


        if(!newFolder.mkdir()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return newFolder.getName();

    }

    @PostMapping("/api/renameFile")
    public void renameFile(@RequestParam String path, @RequestParam String newName,
                           Principal user){
        path = pathToUserPath(path, user);
        File file = new File(path);
        boolean gotRenamed = file.renameTo(new File(Paths.get(path).getParent() + "/" + newName));
        if(!gotRenamed){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/api/fileLink")
    public FileJson getFilesWithLink(@RequestParam String access_code, @RequestParam String path){
        path = validatePath(path);
        if(access_code.equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String mainPath;
        FileLink link = newFolder.findByLink(access_code);
        if(link != null){
            mainPath = link.path;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getJsonFileFromPath(mainPath + path);
    }

    @PostMapping("/api/uploadFile")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("path") String path, Principal user) throws IOException {
        path = String.format("./UsersFiles/%s/%s/%s", user.getName(), path, file.getOriginalFilename());
        file.transferTo(Paths.get(path));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/deleteFile")
    public ResponseEntity deleteFile(@RequestParam("path") String path, Principal user){
        path = pathToUserPath(path, user);
        File fl = new File(path);
        if(!fl.delete()){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/generateLink")
    public String generateProtectedLink(@RequestParam("path") String path, Principal user) throws InterruptedException {
        path = pathToUserPath(path, user);
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        FileLink filelink = new FileLink();
        filelink.path = path;

        FileLink savedLink = newFolder.save(filelink);
        savedLink.code = bcrypt.encode(Integer.toString(savedLink.id));
        savedLink = newFolder.save(filelink);
        // register code in database
        UriComponents build = ServletUriComponentsBuilder.fromCurrentRequestUri().build();
        return URI.create(
                String.format("http://%s:%d/getFiles?access_code=%s",
                        build.getHost(), build.getPort(), savedLink.code)).toString();
    }
    @DeleteMapping("/api/deleteLink")
    public ResponseEntity deleteLink(String path, Principal user){
        path = pathToUserPath(path, user);
        FileLink link = newFolder.findByPath(path);
        if (link == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        newFolder.delete(link);
        return new ResponseEntity(HttpStatus.OK);
    }
//    public String getCurrentUserName(Principal principal){
//        return principal.getName();
//    }
}