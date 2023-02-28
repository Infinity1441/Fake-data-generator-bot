package dev.baxtigul.java_telegram_bots.config;

import com.github.javafaker.domain.Field;
import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.github.javafaker.service.FakerApplicationService;
import dev.baxtigul.java_telegram_bots.daos.UserDao;
import dev.baxtigul.java_telegram_bots.dto.Dictionary;
import dev.baxtigul.java_telegram_bots.handlers.CallbackHandler;
import dev.baxtigul.java_telegram_bots.handlers.Handler;
import dev.baxtigul.java_telegram_bots.handlers.MessageHandler;
import dev.baxtigul.java_telegram_bots.processors.callback.GenerateDataCallbackProcessor;
import dev.baxtigul.java_telegram_bots.processors.message.DefaultMessageProcessor;
import dev.baxtigul.java_telegram_bots.processors.message.GenerateDataMessageProcessor;
import dev.baxtigul.java_telegram_bots.services.UserService;
import dev.baxtigul.java_telegram_bots.state.State;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafeBeansContainer {
    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ThreadLocal<Handler> messageHandler = ThreadLocal.withInitial(MessageHandler::new);
    public static final ThreadLocal<Handler> callbackHandler = ThreadLocal.withInitial(CallbackHandler::new);
    public static final ConcurrentHashMap<Long, State> userState = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Object, Dictionary<String, Object>> collected = new ConcurrentHashMap<>();
    public static final ThreadLocal<FakerApplicationGenerateRequest> fakerApplicationGenerateRequest = ThreadLocal.withInitial(FakerApplicationGenerateRequest::new);
    public static final ThreadLocal<Field> field = ThreadLocal.withInitial(Field::new);
//    public static final Field field1 = field.get();

    public static final ThreadLocal<FakerApplicationService> fakerApplicationService = ThreadLocal.withInitial(FakerApplicationService::new);

    public static final ThreadLocal<UserDao> userDao = ThreadLocal.withInitial(UserDao::new);
    public static final ThreadLocal<UserService> userService = ThreadLocal.withInitial(() -> new UserService(userDao.get()));
    public static final ThreadLocal<GenerateDataCallbackProcessor> generateDataCallbackProcessor = ThreadLocal.withInitial(GenerateDataCallbackProcessor::new);
    public static final ThreadLocal<DefaultMessageProcessor> defaultMessageProcessor = ThreadLocal.withInitial(DefaultMessageProcessor::new);
    public static final ThreadLocal<GenerateDataMessageProcessor> generateDataMessageProcessor = ThreadLocal.withInitial(GenerateDataMessageProcessor::new);
}
