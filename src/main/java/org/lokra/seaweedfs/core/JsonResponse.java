package org.lokra.seaweedfs.core;

/**
 * @author Chiho Sin
 */
public class JsonResponse {

    public final String json;
    public final int statusCode;

    public JsonResponse(String json, int statusCode) {
        this.json = json;
        this.statusCode = statusCode;
    }
}
