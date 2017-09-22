package org.lokra.weed;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.lokra.weed.content.ErrorMessage;
import org.lokra.weed.error.WeedFileException;
import org.lokra.weed.error.WeedServerException;

import java.io.IOException;

import static feign.FeignException.errorStatus;

public class WeedErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public WeedErrorDecoder() {
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            try {
                ErrorMessage errorMessage = objectMapper.readValue(response.body().asReader(), ErrorMessage.class);
                errorMessage.setStatus(response.status());
                errorMessage.setReason(response.reason());
                return new WeedFileException(errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (response.status() >= 500 && response.status() <= 599) {
            try {
                ErrorMessage errorMessage = objectMapper.readValue(response.body().asReader(), ErrorMessage.class);
                errorMessage.setStatus(response.status());
                errorMessage.setReason(response.reason());
                return new WeedServerException(errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return errorStatus(methodKey, response);
    }
}