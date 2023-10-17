package ru.grishuchkov.vkgooglesheetsapibot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.BaseRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;

@Component
public class CallbackRequestMapper {

    public MessageRequest mapMessageNew(JsonNode json){
        MessageRequest request = new MessageRequest();
        setDefaultFields(request, json);

        JsonNode messageNode = json.get("object").get("message");
        request.setUserId(messageNode.get("from_id").asText());
        request.setMessageText(messageNode.get("text").asText());

        return request;
    }

    private void setDefaultFields(BaseRequest baseRequest, JsonNode json){
        baseRequest.setGroupId(json.get("group_id").asLong());
        baseRequest.setRequestType(json.get("type").asText());
        baseRequest.setEventId(json.get("event_id").asText());
        baseRequest.setApiVersion(json.get("v").asText());
    }
}
