package ru.websocketserver.service.message.set;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.websocketserver.domain.incoming.set.Set;
import ru.websocketserver.exception.ProcessException;
import ru.websocketserver.manager.DeviceManager;
import ru.websocketserver.manager.PersonManager;
import ru.websocketserver.service.Device;
import ru.websocketserver.service.message.MessageHandler;

import static ru.websocketserver.util.ErrorMessage.PERSON_DOES_NOT_EXIST;
import static ru.websocketserver.util.ValidationUtil.validateReceivedMessage;

@RequiredArgsConstructor
public abstract class SetMessageHandler<T extends Set> implements MessageHandler {

    private static final Gson gson = new Gson();

    private final Class<T> classMessage;
    private final DeviceManager deviceManager;
    private final PersonManager personManager;

    @Override
    public void handle(WebSocketSession session, TextMessage message) {
        checkPersonRegistration(session.getId());
        String messagePayload = message.getPayload();
        T setSourceMessage = gson.fromJson(messagePayload, classMessage);
        validateReceivedMessage(setSourceMessage);
        Device device = deviceManager.getByMac(setSourceMessage.getPanel());
        device.sendMessage(setSourceMessage);
    }

    private void checkPersonRegistration(String sessionId) {
        if (!personManager.isRegisteredBySessionId(sessionId)) {
            throw new ProcessException(PERSON_DOES_NOT_EXIST);
        }
    }

}