package ru.trackit.ifparser;

import java.util.function.Predicate;

/**
 * == != >= <= > <
 */
public abstract class AbstractExpression {
    public abstract boolean testValue(Object listItem, DataProvider dataProvider);

}
