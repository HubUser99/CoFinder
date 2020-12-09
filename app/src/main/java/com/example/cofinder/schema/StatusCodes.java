package com.example.cofinder.schema;

public abstract class StatusCodes {
    public static String getStatusMessage(int statusCode) {
        String statusMessage = "";

        switch (statusCode) {
            case 400:
                statusMessage = "Bad Request";
                break;
            case 404:
                statusMessage = "Not Found";
                break;
        }

        return statusMessage.isEmpty() ? String.valueOf(statusCode) : statusMessage + " " + statusCode;
    }
}
