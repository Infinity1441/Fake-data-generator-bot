package dev.baxtigul.java_telegram_bots.utils.factory;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;

public class ReplyKeyboardMarkupFactory {
    public static ReplyKeyboardMarkup mainMenu(String language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton(MessageSourceUtils.getLocalizedMessage("main.menu.generate.data", language)));
        replyKeyboardMarkup.selective(true);
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
