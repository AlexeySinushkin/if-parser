import java.util.ArrayList;
import java.util.List;

/**
 * Top level expression
 */
public class CompositeExpression extends AbstractExpression {

     private final char OR = '|';


    /**
     * a==0 || b==3 || c==4 || c<0
     * or a==0 && b==3 && c==4 && c<0
     * or a==0 || b==3 && c==4 || c<0
     * it will test sequentially a,b, then result,c
     * then result,c
     */
    private List<AbstractExpression> flatExpressions = new ArrayList<>();

    /**
     * & | !
     */
    private List<Character> logicOperators = new ArrayList<>();
    private boolean hasOr = false;
    private boolean notExpression = false;

    public CompositeExpression(boolean notFlag)
    {
        notExpression = notFlag;
    }

    void addFlat(AbstractExpression expression) {
        flatExpressions.add(expression);
    }

    void addLogic(char c) {
        logicOperators.add(c);
        if (c == OR) {
            hasOr = true;
        }
    }

    public boolean isEmpty() {
        return flatExpressions.isEmpty();
    }

    @Override
    public boolean testValue(Object listItem, DataProvider dataProvider) {
        boolean result = false;
        //if we have only one item

        if (flatExpressions.size()==1){
            result =  flatExpressions.get(0).testValue(listItem, dataProvider);
        }
        //if only AND and NOT
        else if (!hasOr){
            result=true;
            for (AbstractExpression expression : flatExpressions)
            {
                if (!expression.testValue(listItem, dataProvider))
                {
                    result = false;
                    break;
                }
            }
        }else{
            //if we receive TRUE before or after ||
            // если пользователь написал что то типо a && b || c
            // без скобочек, то он долбаный дурак. найдем b и с
            // если хоть что нибудь TRUE вернем TRUE
             for (int i=0;i<flatExpressions.size();i++){
                AbstractExpression expression = flatExpressions.get(i);
                result = expression.testValue(listItem, dataProvider);
                if (result==true && ((logicOperators.size()>i
                    && logicOperators.get(i)==OR)||(i>0
                        && logicOperators.get(i-1)==OR))){
                    break;
                }
            }
        }


        if (notExpression){
            return !result;
        }
        return result;
    }
}
