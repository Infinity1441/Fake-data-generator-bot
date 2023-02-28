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
import dev.baxtigul.java_telegram_bots.state.DefaultState;
import dev.baxtigul.java_telegram_bots.state.GenerateDataState;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;
import dev.baxtigul.java_telegram_bots.utils.factory.AnswerCallbackQueryFactory;
import dev.baxtigul.java_telegram_bots.utils.factory.SendMessageFactory;
import java.io.File;
import java.nio.file.Path;
import static com.github.javafaker.service.FakerApplicationService.BLACK_LIST;
import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;
import static dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils.getLocalizedMessage;

public class GenerateDataCallbackProcessor implements Processor<GenerateDataState, FakerApplicationGenerateRequest> {
    private final TelegramBot bot = TelegramBotConfiguration.get();

    @Override
    public void process(Update update, GenerateDataState state,FakerApplicationGenerateRequest fakerApplicationGenerateRequest, Field field) {
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
                bot.execute(AnswerCallbackQueryFactory.answerCallbackQuery(callbackQuery.id(), getLocalizedMessage("csv.type.not.supported",language)));
            } else {
                fakerApplicationGenerateRequest.setFileType(FileType.SQL);
                bot.execute(new DeleteMessage(chatID, message.messageId()));
                bot.execute(new SendMessage(chatID, getLocalizedMessage("data.generate.enter.row.count", language)));
                userState.put(chatID, GenerateDataState.ROW_COUNT);
            }
        } else if (state.equals(GenerateDataState.FIELD_TYPE)) {
            FieldType fieldType = FieldType.values()[Integer.parseInt(callbackData) - 1];
            field.setFieldType(fieldType);
            bot.execute(new DeleteMessage(chatID, message.messageId()));

            if (BLACK_LIST.contains(fieldType)) {
                bot.execute(new SendMessage(chatID,getLocalizedMessage("minimum.value",language)));
                userState.put(chatID, GenerateDataState.MIN_VALUE);
            } else {
                field.setMin(0);
                field.setMax(0);
                fakerApplicationGenerateRequest.getFields().add(new Field(field.getFieldName(), field.getFieldType(), field.getMin(), field.getMax()));
                bot.execute(SendMessageFactory.getSendMessageWithAddStopFieldKeyboard(chatID, "data.generate.select.continue.stop", language));
                userState.put(chatID, GenerateDataState.CONFIRM_ADDING_FIELDS);
            }
        } else if (state.equals(GenerateDataState.CONFIRM_ADDING_FIELDS)) {
            bot.execute(new DeleteMessage(chatID, message.messageId()));
            if (callbackData.equals("yes")) {
                bot.execute(new SendMessage(chatID, MessageSourceUtils.getLocalizedMessage("data.generate.enter.field.name", language)));
                userState.put(chatID, GenerateDataState.FIELD_NAME);
            } else {
                String filePath = fakerApplicationService.get().processRequest(fakerApplicationGenerateRequest);
                Path path = Path.of(filePath);
                File file = new File(path.toString());

                userState.put(chatID, DefaultState.MAIN_STATE);
                bot.execute(new SendDocument(chatID, file));
            }
        }
    }
}
