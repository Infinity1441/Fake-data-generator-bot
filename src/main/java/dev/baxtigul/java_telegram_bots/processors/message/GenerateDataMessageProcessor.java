package dev.baxtigul.java_telegram_bots.processors.message;

import com.github.javafaker.domain.Field;
import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.processors.Processor;
import dev.baxtigul.java_telegram_bots.processors.callback.GenerateDataCallbackProcessor;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;
import dev.baxtigul.java_telegram_bots.utils.factory.SendMessageFactory;

import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;

public class GenerateDataMessageProcessor implements Processor<GenerateDataState,FakerApplicationGenerateRequest> {
    private final TelegramBot bot = TelegramBotConfiguration.get();


    @Override
    public void process(Update update, GenerateDataState state,FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        Message message = update.message();
        String text = message.text();
        Long chatID = message.chat().id();
        String language = message.from().languageCode();

        if (state.equals(GenerateDataState.FILE_NAME)) {
            fakerApplicationGenerateRequest.setFileName(text);
            bot.execute(SendMessageFactory.getSendMessageWithFileTypeKeyboard(chatID, "data.generate.enter.file.type", language));
            userState.put(chatID, GenerateDataState.FILE_TYPE);
        } else if (state.equals(GenerateDataState.ROW_COUNT)) {
            if (!siValueDigit(text)) {
                bot.execute(new DeleteMessage(chatID, message.messageId()));
            } else {
                int rowCount = Integer.parseInt(text);
                if (rowCount > 20_000) {
                    bot.execute(new SendMessage(chatID, MessageSourceUtils.getLocalizedMessage("data.generate.too.many.rows", language)));
                } else {
                    fakerApplicationGenerateRequest.setCount(rowCount);
                    bot.execute(new SendMessage(chatID, MessageSourceUtils.getLocalizedMessage("data.generate.enter.field.name", language)));
                    userState.put(chatID, GenerateDataState.FIELD_NAME);
                }
            }
        } else if (state.equals(GenerateDataState.FIELD_NAME)) {
            field1.setFieldName(text);
            bot.execute(SendMessageFactory.getSendMessageWithFieldTypes(chatID, "data.generate.select.field.type", language));
            userState.put(chatID, GenerateDataState.FIELD_TYPE);
        } else if (state.equals(GenerateDataState.MIN_VALUE)) {
            if (!siValueDigit(text)) {
                bot.execute(new DeleteMessage(chatID, message.messageId()));
            } else {
                field1.setMin(Integer.parseInt(text));
                // TODO localize
                bot.execute(new SendMessage(chatID, "Maximum value: "));
                userState.put(chatID, GenerateDataState.MAX_VALUE);
            }
        } else if (state.equals(GenerateDataState.MAX_VALUE)) {
            if (!siValueDigit(text)) {
                bot.execute(new DeleteMessage(chatID, message.messageId()));
            } else {
                field1.setMax(Integer.parseInt(text));
                fakerApplicationGenerateRequest.getFields().add(new Field(field1.getFieldName(),field1.getFieldType(),field1.getMin(),field1.getMax()));
                // TODO localize
                bot.execute(SendMessageFactory.getSendMessageWithAddStopFieldKeyboard(chatID, "data.generate.select.continue.stop", language));
                userState.put(chatID, GenerateDataState.CONFIRM_ADDING_FIELDS);
            }
        } else if (state.equals(GenerateDataState.FILE_TYPE))
            bot.execute(new DeleteMessage(chatID, message.messageId()));
    }

    private boolean siValueDigit(String value) {
        return value.matches("\\d*");
    }
}
