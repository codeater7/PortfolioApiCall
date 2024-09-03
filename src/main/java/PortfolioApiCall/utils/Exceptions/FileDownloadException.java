package PortfolioApiCall.utils.Exceptions;

public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message, Throwable cause){
        super( message, cause);
    }
}
