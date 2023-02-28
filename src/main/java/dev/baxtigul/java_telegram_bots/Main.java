package dev.baxtigul.java_telegram_bots;

import com.pengrad.telegrambot.UpdatesListener;
import dev.baxtigul.java_telegram_bots.config.InitializerConfiguration;
import dev.baxtigul.java_telegram_bots.config.TelegramBotConfiguration;
import dev.baxtigul.java_telegram_bots.handlers.UpdateHandler;
import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;

public class Main {
    public static void main(String[] args) {
        InitializerConfiguration.init();
        UpdateHandler updateHandler = new UpdateHandler();
        TelegramBotConfiguration.get().setUpdatesListener((updates) -> {
            updateHandler.handle(updates, fakerApplicationGenerateRequest.get(),field.get());
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}