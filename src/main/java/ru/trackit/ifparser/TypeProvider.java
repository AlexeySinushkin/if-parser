package ru.trackit.ifparser;

@FunctionalInterface
public interface TypeProvider {
    Class getType(String fieldName) throws ReflectiveOperationException;
}
