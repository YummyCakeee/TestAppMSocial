package ru.nikita.test.utils.enums;

public enum LoaderType {
    SQL("sql"), IN_MEMORY("inMemory");

    private String type;

    public String getAction()
    {
        return this.type;
    }

    private LoaderType(String type)
    {
        this.type = type;
    }
}
