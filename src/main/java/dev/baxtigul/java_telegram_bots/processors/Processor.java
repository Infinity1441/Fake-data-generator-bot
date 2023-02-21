package dev.baxtigul.java_telegram_bots.processors;

import com.github.javafaker.service.FakerApplicationGenerateRequest;
import com.pengrad.telegrambot.model.Update;

public interface Processor<S,T> {
    void process(Update update, S state, T fakerApplicationGenerateRequest);
}
