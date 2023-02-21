package dev.baxtigul.java_telegram_bots.utils.factory;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.baxtigul.java_telegram_bots.utils.BaseUtils;
import dev.baxtigul.java_telegram_bots.utils.MessageSourceUtils;

import java.util.Objects;

public class InlineKeyboardMarkupFactory {
    public static InlineKeyboardMarkup enterPasswordKeyboard() {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        replyMarkup.addRow(
                getInlineButton(1, 1),
                getInlineButton(2, 2),
                getInlineButton(3, 3)
        );
        replyMarkup.addRow(
                getInlineButton(4, 4),
                getInlineButton(5, 5),
                getInlineButton(6, 6)
        );
        replyMarkup.addRow(
                getInlineButton(7, 7),
                getInlineButton(8, 8),
                getInlineButton(9, 9)
        );
        replyMarkup.addRow(
                getInlineButton(0, 0),
                getInlineButton(BaseUtils.TICK, "done"),
                getInlineButton(BaseUtils.CLEAR, "d")
        );
        return replyMarkup;
    }

    private static InlineKeyboardButton getInlineButton(final Object text, final Object callbackData) {
        var button = new InlineKeyboardButton(Objects.toString(text));
        button.callbackData(Objects.toString(callbackData));
        return button;
    }


    public static SendMessage getSendMessageWithPasswordKeyboard(Object chatID, String message) {
        SendMessage sendMessage = new SendMessage(chatID, message);
        sendMessage.replyMarkup(enterPasswordKeyboard());
        return sendMessage;
    }

    public static InlineKeyboardMarkup getFileTypeKeyboard() {
        return new InlineKeyboardMarkup(
                getInlineButton("JSON", "json"),
                getInlineButton("CSV", "csv"),
                getInlineButton("SQL", "sql")
        );
    }

    public static InlineKeyboardMarkup getFieldTypeKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                getInlineButton(1,1),
                getInlineButton(2,2),
                getInlineButton(3,3),
                getInlineButton(4,4)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(5,5),
                getInlineButton(6,6),
                getInlineButton(7,7),
                getInlineButton(8,8)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(9,9),
                getInlineButton(10,10),
                getInlineButton(11,11),
                getInlineButton(12,12)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(13,13),
                getInlineButton(14,14),
                getInlineButton(15,15),
                getInlineButton(16,16)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(17,17),
                getInlineButton(18,18),
                getInlineButton(19,19),
                getInlineButton(20,20)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(21,21),
                getInlineButton(22,22),
                getInlineButton(23,23),
                getInlineButton(24,24)
                );
        inlineKeyboardMarkup.addRow(
                getInlineButton(25,25)
                );
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getConfirmationButton(String language) {
        return new InlineKeyboardMarkup(
                getInlineButton(MessageSourceUtils.getLocalizedMessage("data.generate.yes.button",language),"yes"),
                getInlineButton(MessageSourceUtils.getLocalizedMessage("data.generate.no.button",language),"no")
        );
    }
}
