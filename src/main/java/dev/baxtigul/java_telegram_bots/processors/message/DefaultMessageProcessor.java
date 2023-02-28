package dev.baxtigul.java_telegram_bots.processors.message;

import com.github.javafaker.domain.Field;
import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.processors.Processor;
import dev.baxtigul.java_telegram_bots.state.DefaultState;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.utils.factory.SendMessageFactory;

import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.userState;
import static dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils.getLocalizedMessage;

public class DefaultMessageProcessor implements Processor<DefaultState,FakerApplicationGenerateRequest> {
    private final TelegramBot bot = TelegramBotConfiguration.get();

    @Override
    public void process(Update update, DefaultState state, FakerApplicationGenerateRequest fakerApplicationGenerateRequest, Field field) {
        Message message = update.message();
        User from = message.from();
        String text = message.text();
        Long chatID = message.chat().id();
        String language = message.from().languageCode();
        if ( state.equals(DefaultState.DELETE) ) {
            bot.execute(new DeleteMessage(chatID, message.messageId()));
        } else if ( state.equals(DefaultState.MAIN_STATE) ) {
            if ( text.equals(getLocalizedMessage("main.menu.generate.data", language)) ) {
                bot.execute(new SendMessage(chatID, getLocalizedMessage("data.generate.enter.file.name",language)));
                userState.put(chatID, GenerateDataState.FILE_NAME);
            } else {
                bot.execute(SendMessageFactory.sendMessageWithMainMenu(chatID, "Feel free to use a bot\uD83D\uDE07", from.languageCode()));
            }
        }
    }
}
