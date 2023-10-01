package com.example.springapp.exceptions;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class IsNoRecordWithTempForToday extends RuntimeException{
    public IsNoRecordWithTempForToday(String message) {
        super(message);
    }

    public String printError(String message, int status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("status", status);
        jsonObject.put("dateTime", LocalDateTime.now());
        return jsonObject.toString();
    }
}
