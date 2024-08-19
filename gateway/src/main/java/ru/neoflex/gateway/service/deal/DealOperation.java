package ru.neoflex.gateway.service.deal;

public enum DealOperation {

    FINISH_REGISTRATION("/calculate/*"),
    REQUEST_DOCUMENTS("/document/*/send"),
    REQUEST_SIGN("/document/*/sign"),
    CODE_SIGN("/document/*/code"),
    GET_STATEMENT_BY_ID("/admin/statement/*"),
    GET_ALL_STATEMENTS("/admin/statement");

    private final String path;

    DealOperation(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
