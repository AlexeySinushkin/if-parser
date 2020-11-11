public class DoubleExpression extends AbstractExpression {
    private String left;
    private double right;
    private ExpressionTypes expressionTypes;

    public DoubleExpression(String left, double right, ExpressionTypes exp)
    {
        this.left=left;
        this.right=right;
        this.expressionTypes=exp;
    }

    public boolean testValue(Object listItem, DataProvider dataProvider) {
        double leftValue = (double)dataProvider.getValue(listItem, left);
        switch (expressionTypes){
            case More:
                return leftValue>right;
            case MoreOrEqual:
                return leftValue>=right;
            case Less:
                return leftValue<right;
            case LessOrEqual:
                return leftValue<=right;
            case Equal:
                return leftValue==right;
            case NotEqual:
                return leftValue!=right;
        }
        return false;
    }
}
