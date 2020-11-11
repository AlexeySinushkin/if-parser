package ru.trackit.ifparser;

/**
 * == != >= <= > <
 */
public abstract class AbstractExpression {
    public abstract boolean testValue(Object listItem, DataProvider dataProvider);
}
