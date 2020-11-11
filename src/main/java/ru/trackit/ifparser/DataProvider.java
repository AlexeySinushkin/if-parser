package ru.trackit.ifparser;
import java.time.format.DateTimeFormatter;

/**
 * источник данных
 * @param <T> List<key, value>
 */
public interface DataProvider<T> {
     Object getValue(T item, String fieldName);

     Class getTypeOrNull(String fieldName);

     final static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
     default DateTimeFormatter getDateTimeFormatter()
     {
          return formatter;
     }
}
