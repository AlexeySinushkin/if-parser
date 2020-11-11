package ru.trackit.ifparser;
public class ParseException extends Exception {

    private final String expression;

    public ParseException(String expression)
    {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ParseException{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
