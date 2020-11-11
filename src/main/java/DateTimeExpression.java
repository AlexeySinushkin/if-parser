import java.time.LocalDateTime;

public class DateTimeExpression extends AbstractExpression {
    private String left;
    private LocalDateTime right;
    private ExpressionTypes expressionTypes;

    public DateTimeExpression(String left, LocalDateTime right, ExpressionTypes exp)
    {
        this.left=left;
        this.right=right;
        this.expressionTypes=exp;
    }

    public boolean testValue(Object listItem, DataProvider dataProvider) {
        LocalDateTime leftValue = (LocalDateTime)dataProvider.getValue(listItem, left);
        switch (expressionTypes){
            case Equal:
                return leftValue.equals(right);
            case More:
                return leftValue.isAfter(right);
            case MoreOrEqual:
                return leftValue.isAfter(right) || leftValue.equals(right);
            case Less:
                return leftValue.isBefore(right);
            case LessOrEqual:
                return leftValue.isBefore(right)|| leftValue.equals(right);
            case NotEqual:
                return !leftValue.equals(right);
        }
        return false;
    }
}
