package dev.baxtigul.java_telegram_bots.utils.factory;

import com.github.javafaker.domain.FieldType;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.commons.lang3.StringUtils;

import static dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils.getLocalizedMessage;

public class SendMessageFactory {
    public static EditMessageText getEditMessageTextForPassword(Object chatID, int messageID, String messageText) {
        EditMessageText editMessageText = new EditMessageText(chatID, messageID, messageText);
        editMessageText.replyMarkup(InlineKeyboardMarkupFactory.enterPasswordKeyboard());
        return editMessageText;
    }

    public static SendMessage sendMessageWithMainMenu(Object chatID, String messageText, String language) {
        SendMessage sendMessage = new SendMessage(chatID, messageText);
        sendMessage.replyMarkup(ReplyKeyboardMarkupFactory.mainMenu(language));
        return sendMessage;
    }


    public static SendMessage getSendMessageWithFileTypeKeyboard(Long chatID, String key, String language) {
        SendMessage sendMessage = new SendMessage(chatID, getLocalizedMessage(key, language));
        sendMessage.replyMarkup(InlineKeyboardMarkupFactory.getFileTypeKeyboard());
        return sendMessage;
    }

    public static SendMessage getSendMessageWithFieldTypes(Long chatID, String key, String language) {
        StringBuilder message = new StringBuilder(getLocalizedMessage(key, language));
        message.append("\n");
        int i = 1;
        for (FieldType value : FieldType.values()) {
            message.append("%2d. %-20s".formatted(i, value)).append("\n");
            i++;
        }
        SendMessage sendMessage = new SendMessage(chatID, String.valueOf(message));
        sendMessage.replyMarkup(InlineKeyboardMarkupFactory.getFieldTypeKeyboard());
        return sendMessage;
    }

    public static SendMessage getSendMessageWithAddStopFieldKeyboard(Long chatID, String key, String language) {
        SendMessage sendMessage = new SendMessage(chatID, getLocalizedMessage(key, language));
        sendMessage.replyMarkup(InlineKeyboardMarkupFactory.getConfirmationButton(language));
        return sendMessage;
    }
}
