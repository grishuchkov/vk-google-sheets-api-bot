package ru.grishuchkov.vkgooglesheetsapibot.exception.handle;


import com.vk.api.sdk.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.grishuchkov.vkgooglesheetsapibot.exception.handle.dto.SenlerBodyResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.OK)
    SenlerBodyResponse handleResourceNotFoundException(ApiException e) {
        Integer exceptionCode = e.getCode();
        if (exceptionCode == 15){
            return SenlerBodyResponse.construct(String.valueOf(HttpStatus.FORBIDDEN.value()), e.getMessageRaw());
        }
        if (exceptionCode == 100){
            return SenlerBodyResponse.construct(String.valueOf(HttpStatus.CONFLICT.value()), e.getMessageRaw());
        }

        return SenlerBodyResponse.construct(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessageRaw());
    }
}
