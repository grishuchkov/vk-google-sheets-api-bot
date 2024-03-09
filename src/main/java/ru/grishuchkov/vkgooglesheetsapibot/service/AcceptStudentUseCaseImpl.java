package ru.grishuchkov.vkgooglesheetsapibot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentResponse;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.AcceptStudentUseCase;

@Service
@RequiredArgsConstructor
public class AcceptStudentUseCaseImpl implements AcceptStudentUseCase {

    private final VkApiClient vkApiClient;

    @Override
    public AcceptStudentResponse accept(AcceptStudentRequest request) {

        String mainGroupToken = "vk1.a.Vl3u8Lpn1VwdS7zPIBF1CAEEgxrsd8vdecQ_C1p6r5DR57pm4c-S41tUz4OfeYKo24h73xEQL0pjoYzSl9YFAxmC0Q9RxiX4z_2tQF-sfv-TEy08qT5Lr2GIrJKqbyNNoX9qjhZ2YnUNkbu_p27yTXACjlEopjUK6b_ayvd_qC5qyf3wcWv-y96bkKWxBw_yE5OQDbxkNPRDl6h1afUZ1A";
        vkApiClient.approveRequest(request.groupId(), mainGroupToken, request.userId());

        return new AcceptStudentResponse();
    }
}
