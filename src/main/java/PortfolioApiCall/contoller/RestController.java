package PortfolioApiCall.contoller;

import PortfolioApiCall.Config.EnvVariableReader;
import PortfolioApiCall.service.Files.CsvWriterService;
import PortfolioApiCall.service.Files.FileService;
import PortfolioApiCall.service.GraphQlDataFetcher;
import PortfolioApiCall.service.RestDataFetcher;
import PortfolioApiCall.utils.Exceptions.UnprocessableEntityException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Path;
import java.util.HashMap;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/portfolio")
@Configuration

public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    @Autowired
    private EnvVariableReader envVariableReader;
    @Autowired
    private GraphQlDataFetcher graphQlDataFetcher;
    @Autowired
    private FileService fileService;
    @Autowired
    private CsvWriterService csvWriterService;
    @Autowired
    private RestDataFetcher restDataFetcher;

    private HashMap<String, Object> paramsResolver(String id, String startDate, String endDate) {

        var params = new HashMap<String, Object>();


        if (null == id || id.isEmpty()) {
            throw new UnprocessableEntityException("No Id provided");
        } else {
            params.put("id", Integer.parseInt(id));
        }

        if (null == startDate || startDate.isEmpty()) {
            params.put("startDate", "start");
        } else {
            params.put("startDate", startDate);
        }

        if (null == endDate || endDate.isEmpty()) {
            params.put("endDate", "end");
        } else {
            params.put("endDate", endDate);
        }

        return params;


    }


    @GetMapping("/{id}")
    public ResponseEntity<String> fetchPortfolioAndSaveToCsv(@PathVariable(value = "id") String id, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) {


        HashMap<String, Object> params = paramsResolver(id, startDate, endDate);

        Integer portfolioId = (Integer) params.get("id");
        String start = (String) params.get("startDate");
        String end = (String) params.get("endDate");

        try {
            // JsonNode dummyData = CsvWriterService.DummyData();
            JsonNode transactions = graphQlDataFetcher.getTransactions(portfolioId, start, end);
            Path file_path = fileService.createFile(portfolioId, start, end);
            String response = csvWriterService.writeToCsv(transactions, file_path);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while creating and writing to file " + e.getMessage());
            throw new RuntimeException("File Creation Failed");
        }

    }

    @GetMapping("/{id}/readonly")
    public ResponseEntity<JsonNode> fetchPortfolio(@PathVariable(value = "id") String id, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) {

        HashMap<String, Object> params = paramsResolver(id, startDate, endDate);

        Integer portfolioId = (Integer) params.get("id");
        String start = (String) params.get("startDate");
        String end = (String) params.get("endDate");

        try {
            JsonNode response = graphQlDataFetcher.getTransactions(portfolioId, start, end);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while fetching the data " + e.getMessage());
            throw new RuntimeException("Error while fetching the data ");
        }
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "id") String id, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) {

        HashMap<String, Object> params = paramsResolver(id, startDate, endDate);

        Integer portfolioId = (Integer) params.get("id");
        String start = (String) params.get("startDate");
        String end = (String) params.get("endDate");

        Resource resource = fileService.downloadFile(portfolioId, start, end);
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + resource.getFilename() + "\"").body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // for testing the credentials API to FA server from the application.
    @GetMapping("/fetch-credentials")
    public String fetchCredentials() {
        return restDataFetcher.getFACredentials();
    }
}



