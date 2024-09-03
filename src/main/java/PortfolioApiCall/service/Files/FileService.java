package PortfolioApiCall.service.Files;

import PortfolioApiCall.Config.EnvVariableReader;
import PortfolioApiCall.contoller.RestController;
import PortfolioApiCall.utils.Exceptions.FileDownloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Autowired
    EnvVariableReader configurationReader;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public Path createFile(Integer Id, String startDate, String endDate) {

        try {
            Path rootDir = Paths.get(configurationReader.getRootDir());

            if (!Files.exists(rootDir)) {
                Files.createDirectory(rootDir);
            }

            // Create a sub Directory with the id
            Path subDir = rootDir.resolve(Id.toString());
            if (!Files.exists(subDir)) {
                Files.createDirectory(subDir);
            }

            // Create a file with the name based on start date and endDate
            String fileName = startDate + "-" + endDate + ".csv";

            Path filePath = subDir.resolve(fileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            return filePath;


        } catch (IOException e) {
            throw new FileDownloadException("Error at downloadFiles " + Id, e);
        }

    }

    public String readFile(String Id, String startDate, String endDate) {
        Path filePath = Paths.get(configurationReader.getRootDir(), Id, startDate + "-" + endDate + ".csv");
        if (Files.exists(filePath)) {
            return "File Exists";
        } else {
            return "File does not Exist";
        }

    }

    public String deleteFile(String Id, String startDate, String endDate) {
        Path filePath = Paths.get(configurationReader.getRootDir(), Id, startDate + "-" + endDate + ".csv");
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return "File deleted";
            } else {
                return "File does not Exist";
            }

        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }
    }

    public Resource downloadFile(Integer Id, String startDate, String endDate){
        try{
            Path filePath = Paths.get(configurationReader.getRootDir(),Id.toString(), startDate+ "-" + endDate + ".csv");
            return new UrlResource(filePath.toUri());

        } catch (IOException e){
            logger.info("Error at  downloadFile " + e.getMessage() );
            throw new FileDownloadException("Error at downloadFiles " + Id, e);
        }
    }



}
