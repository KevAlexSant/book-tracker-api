package org.kashboikev.booklist.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private long timeStamp;


    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
    }


}
