package dev.baxtigul.java_telegram_bots.processors.callback;

import com.github.javafaker.domain.Field;
import com.github.javafaker.domain.FieldType;
import com.github.javafaker.domain.FileType;
import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.processors.Processor;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;
import dev.baxtigul.java_telegram_bots.utils.factory.AnswerCallbackQueryFactory;
import dev.baxtigul.java_telegram_bots.utils.factory.SendMessageFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import static com.github.javafaker.service.FakerApplicationService.BLACK_LIST;
import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;
import static dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils.getLocalizedMessage;

public class GenerateDataCallbackProcessor implements Processor<GenerateDataState,FakerApplicationGenerateRequest> {
    private final TelegramBot bot = TelegramBotConfiguration.get();

    @Override
    public void process(Update update, GenerateDataState state,FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String callbackData = callbackQuery.data();
        Chat chat = message.chat();
        Long chatID = chat.id();
        String language = callbackQuery.from().languageCode();
        if (state.equals(GenerateDataState.FILE_TYPE)) {

            if (callbackData.equals("json")) {
                fakerApplicationGenerateRequest.setFileType(FileType.JSON);
                bot.execute(new DeleteMessage(chatID, message.messageId()));
                bot.execute(new SendMessage(chatID, getLocalizedMessage("data.generate.enter.row.count", language)));
                userState.put(chatID, GenerateDataState.ROW_COUNT);
            } else if (callbackData.equals("csv")) {
                // TODO: 05/02/23 localize here
                bot.execute(AnswerCallbackQueryFactory.answerCallbackQuery(callbackQuery.id(), "Sorry This CSV Type not supported yet"));
            } else {
                // TODO: 05/02/23 localize here
                bot.execute(AnswerCallbackQueryFactory.answerCallbackQuery(callbackQuery.id(), "Sorry This SQL Type not supported yet"));
            }
        } else if (state.equals(GenerateDataState.FIELD_TYPE)) {
            FieldType fieldType = FieldType.values()[Integer.parseInt(callbackData) - 1];
            field1.setFieldType(fieldType);

            bot.execute(new DeleteMessage(chatID, message.messageId()));

            if (BLACK_LIST.contains(fieldType)) {
                // TODO localize 06/02/2023
                bot.execute(new SendMessage(chatID, "Minimum value:"));
                userState.put(chatID, GenerateDataState.MIN_VALUE);
            } else {
                field1.setMin(0);
                field1.setMax(0);
                fakerApplicationGenerateRequest.getFields().add(new Field(field1.getFieldName(), field1.getFieldType(), field1.getMin(), field1.getMax()));
                bot.execute(SendMessageFactory.getSendMessageWithAddStopFieldKeyboard(chatID, "data.generate.select.continue.stop", language));
                userState.put(chatID, GenerateDataState.CONFIRM_ADDING_FIELDS);
            }
        } else if (state.equals(GenerateDataState.CONFIRM_ADDING_FIELDS)) {
            bot.execute(new DeleteMessage(chatID, message.messageId()));
            if (callbackData.equals("yes")) {
                bot.execute(new SendMessage(chatID, MessageSourceUtils.getLocalizedMessage("data.generate.enter.field.name", language)));
                userState.put(chatID, GenerateDataState.FIELD_NAME);
            } else {
                String result = fakerApplicationService.get().processRequest(fakerApplicationGenerateRequest);
                String fileName = fakerApplicationGenerateRequest.getFileName() + "." + fakerApplicationGenerateRequest.getFileType().toString().toLowerCase();
                Path path = Path.of(fileName);
                try {
                    if (Files.notExists(path))
                        Files.createFile(path);
                    Files.writeString(path, result, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(path.toString());
                bot.execute(new SendDocument(chatID, file));
            }
        }

    }
}
