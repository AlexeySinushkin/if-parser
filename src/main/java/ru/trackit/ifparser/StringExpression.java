package ru.trackit.ifparser;
import java.util.regex.Pattern;

public class StringExpression extends AbstractExpression {
    private String left;
    private String right;
    private ExpressionTypes expressionTypes;

    public StringExpression(String left, String right, ExpressionTypes exp)
    {
        this.left=left;
        this.right=right;
        this.expressionTypes=exp;
    }

    public boolean testValue(Object listItem, DataProvider dataProvider) {
        String leftValue = (String)dataProvider.getValue(listItem, left);
        switch (expressionTypes){
            case Equal:
                return right.equalsIgnoreCase(leftValue);
            case NotEqual:
                return !right.equalsIgnoreCase(leftValue);
        }
        return false;
    }
}
