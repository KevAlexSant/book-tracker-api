package org.kashboikev.booklist.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String message;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timeStamp;


    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = ZonedDateTime.now();
    }


}
