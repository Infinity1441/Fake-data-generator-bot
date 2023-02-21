package dev.baxtigul.java_telegram_bots.handlers;

import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.model.Update;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.*;

public class UpdateHandler {
    public void handle(List<Update> updates, FakerApplicationGenerateRequest fakerApplicationGenerateRequest) {
        CompletableFuture.runAsync(() -> {
            for ( Update update : updates ) {
                executor.submit(() -> {
                    if ( Objects.nonNull(update.message()) )
                        messageHandler.get().handle(update,fakerApplicationGenerateRequest);
                    else if ( Objects.nonNull(update.callbackQuery()) )
                        callbackHandler.get().handle(update,fakerApplicationGenerateRequest);
                });
            }
        });
    }
}
