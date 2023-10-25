package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.responses.GetCallbackConfirmationCodeResponse;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.messages.MessagesSendMessageEventAnswerQuery;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.exception.VkClientException;

import java.util.List;
import java.util.Random;

@Service
public class VkClient implements ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient {

    @Value("${vk.api.token}")
    private String token;
    private final Random random;
    private final VkApiClient vk;

    public VkClient(VkApiClient vk) {
        this.vk = vk;
        random = new Random();
    }

    private void executeRequest(AbstractQueryBuilder<?, ?> request){
        try {
            Object execute = request.execute();
        } catch (ApiException | ClientException e) {
            throw new VkClientException(e);
        }
    }

    @Override
    public void sendMessage(Integer groupId, Integer userId, String text) {
        sendMessage(groupId, userId, text, null);
    }

    @Override
    public void sendMessage(Integer groupId, Integer userId, String text, Keyboard keyboard) {
        Keyboard keyboardObject = new Keyboard();
        if (keyboard != null) {
            keyboardObject = keyboard;
        }

        MessagesSendQuery request = vk.messages()
                .send(new GroupActor(groupId, token))
                .userId(userId)
                .message(text)
                .keyboard(keyboardObject)
                .randomId(random.nextInt());

        executeRequest(request);
    }

    @Override
    public void sendMessageEventAnswer(Integer groupId, Integer userId, Integer peerId, String eventId) {

        MessagesSendMessageEventAnswerQuery request = vk.messages()
                .sendMessageEventAnswer(new GroupActor(groupId, token), eventId, userId, peerId)
                .eventData("{\"type\": \"message\"," +
                        "\"text\": \"Мы очень пытаемся отправить вашу работу на проверку!\"}");

        executeRequest(request);
    }

    @Override
    @SneakyThrows
    public String getConfirmationCode(Integer groupId) {
        GetCallbackConfirmationCodeResponse response = vk.groups()
                .getCallbackConfirmationCode(new GroupActor(groupId, token), groupId).execute();

        return response.getCode();
    }

    @SneakyThrows
    @Override
    public String getUserScreenNameByUserId(int groupId, int userId) {
        List<GetResponse> response = vk.users()
                .get(new GroupActor(groupId, token))
                .userIds(String.valueOf(userId))
                .fields(Fields.SCREEN_NAME)
                .execute();


        if(response.isEmpty()){
            throw new RuntimeException("getUserScreenNameByUserId failed");
        }

        return response.get(0).getScreenName();
    }

    @SneakyThrows
    @Override
    public Integer getUserIdByScreenName(int groupId, String screenName) {
        List<GetResponse> response = vk.users()
                .get(new GroupActor(groupId, token))
                .userIds(screenName)
                .execute();

        if(response.isEmpty()){
            throw new RuntimeException("getUserIdByScreenName failed");
        }

        return response.get(0).getId();
    }
}
