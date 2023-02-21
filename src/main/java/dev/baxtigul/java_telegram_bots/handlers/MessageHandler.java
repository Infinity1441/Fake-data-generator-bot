package dev.baxtigul.java_telegram_bots.handlers;

import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.domains.UserDomain;
import dev.baxtigul.java_telegram_bots.state.DefaultState;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.state.State;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;
import dev.baxtigul.java_telegram_bots.utils.factory.ReplyKeyboardMarkupFactory;
import lombok.NonNull;

import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;


/*@Slf4j*/
public class MessageHandler implements Handler {
    private final TelegramBot bot = TelegramBotConfiguration.get();

    @Override
    public void handle(Update update, FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        Message message = update.message();
        Long chatID = message.chat().id();
        State state = userState.get(chatID);
        String languageCode = message.from().languageCode();

        if (message.text().equals(MessageSourceUtils.getLocalizedMessage("main.menu.generate.data",languageCode)))
            userState.put(chatID,DefaultState.MAIN_STATE);

        if (state == null) {
            startRegister(message, languageCode);
        } else if (state instanceof DefaultState defaultState)
            defaultMessageProcessor.get().process(update, defaultState,fakerApplicationGenerateRequest);
        else if (state instanceof GenerateDataState generateDataState)
            generateDataMessageProcessor.get().process(update, generateDataState,fakerApplicationGenerateRequest);
    }

    private void startRegister(@NonNull Message message, String languageCode) {
        Long chatID = message.chat().id();
        userService.get().create(UserDomain.builder()
                        .chatID(chatID)
                        .username(message.from().username())
                        .firstName(message.from().firstName())
                        .language(languageCode)
                .build());
        userState.put(chatID, DefaultState.DELETE);
        SendMessage sendMessage = new SendMessage(chatID, "Welcome to the fake data generator bot");
        sendMessage.replyMarkup(ReplyKeyboardMarkupFactory.mainMenu(languageCode));
        bot.execute(sendMessage);
    }
}
