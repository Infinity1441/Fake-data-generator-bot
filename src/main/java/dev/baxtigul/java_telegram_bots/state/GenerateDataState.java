package dev.baxtigul.java_telegram_bots.state;

public enum GenerateDataState implements State {
    FILE_NAME,
    FILE_TYPE,
    ROW_COUNT,
    FIELDS,
    FIELD_NAME, FIELD_TYPE, CONFIRM_ADDING_FIELDS, MAX_VALUE, MIN_VALUE, GENERATE
}
