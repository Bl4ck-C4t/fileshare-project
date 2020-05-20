package org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses;

public class ServedFile {
    public String fileName;
    public String fileContent;

    public ServedFile(String fileName, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
