package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.groups.responses.GetCallbackConfirmationCodeResponse;
import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VkClient implements ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient {

    @Value("${apiToken}")
    private String token;
    private final Random random;
    private final VkApiClient vk;

    public VkClient(VkApiClient vk) {
        this.vk = vk;
        random = new Random();
    }

    @Override
    @SneakyThrows
    public void sendMessage(Integer groupId, String text, Integer userId) {
        sendMessage(groupId, text, userId, null);
    }

    @Override
    @SneakyThrows
    public void sendMessage(Integer groupId, String text, Integer userId, Keyboard keyboard){
        Keyboard keyboardObject = new Keyboard();
        if(keyboard != null){
            keyboardObject = keyboard;
        }

        vk.messages()
                .send(new GroupActor(groupId, token))
                .userId(userId)
                .message(text)
                .keyboard(keyboardObject)
                .randomId(random.nextInt())
                .execute();
    }

    @Override
    @SneakyThrows
    public String getConfirmationCode(Integer groupId) {
        GetCallbackConfirmationCodeResponse response = vk.groups()
                .getCallbackConfirmationCode(new GroupActor(groupId, token), groupId).execute();

        return response.getCode();
    }
}
