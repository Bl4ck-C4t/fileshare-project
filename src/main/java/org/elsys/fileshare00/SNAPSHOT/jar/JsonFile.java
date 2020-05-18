package org.elsys.fileshare00.SNAPSHOT.jar;

import java.util.List;

public class JsonFile {
    public boolean isDirectory;
    public List<String> files;
    public String fileContent;
    public String fileName;

    public JsonFile(boolean isDirectory, List<String> files, String fileContent, String fileName) {
        this.isDirectory = isDirectory;
        this.files = files;
        this.fileContent = fileContent;
        this.fileName = fileName;
    }
}
