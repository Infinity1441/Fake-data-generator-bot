package dev.baxtigul.java_telegram_bots.handlers;

import com.github.javafaker.domain.Field;
import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.model.Update;

public interface Handler {
    void handle(Update update, FakerApplicationGenerateRequest fakerApplicationGenerateRequest, Field field);
}
