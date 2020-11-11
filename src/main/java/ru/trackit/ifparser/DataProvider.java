package ru.trackit.ifparser;
import java.time.format.DateTimeFormatter;

/**
 * источник данных
 * @param <T> List<key, value>
 */

public interface DataProvider<T> {
     Object getValue(T item, String fieldName);
}
