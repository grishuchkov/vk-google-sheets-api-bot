package ru.grishuchkov.vkgooglesheetsapibot.service.ifcs;

import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentResponse;

public interface AcceptStudentUseCase {
    AcceptStudentResponse accept(AcceptStudentRequest request);
}
