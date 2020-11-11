package ru.trackit.ifparser;
class IntegerExpression extends AbstractExpression {
    private String left;
    private int right;
    private ExpressionTypes expressionTypes;

    public IntegerExpression(String left, int right, ExpressionTypes exp)
    {
        this.left=left;
        this.right=right;
        this.expressionTypes=exp;
    }

    public boolean testValue(Object listItem, DataProvider dataProvider) {
        int leftValue = (int)dataProvider.getValue(listItem, left);
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
