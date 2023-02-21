package dev.baxtigul.java_telegram_bots.handlers;

import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.state.RegistrationState;
import dev.baxtigul.java_telegram_bots.state.State;

import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;

public class CallbackHandler implements Handler {
    @Override
    public void handle(Update update, FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Long chatID = callbackQuery.message().chat().id();
        State state = userState.get(chatID);
        if ( state instanceof GenerateDataState generateDataState )
            generateDataCallbackProcessor.get().process(update, generateDataState,fakerApplicationGenerateRequest);
    }
}
