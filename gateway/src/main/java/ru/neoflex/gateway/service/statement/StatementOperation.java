package ru.neoflex.gateway.service.statement;

public enum StatementOperation {

    PRESCORE_AND_GENERATE_OFFERS(""),
    CHOOSE_OFFER("/offer");

    private final String path;

    StatementOperation(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
