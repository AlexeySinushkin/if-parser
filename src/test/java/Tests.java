import org.junit.After;
import org.junit.Test;
import ru.trackit.ifparser.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tests {


    public class TestObject {
        public String textField;
        public int integerField;
        public double doubleField;
        public LocalDateTime datetimeField;

        public TestObject(String textField, int integerField,
                          double doubleField, LocalDateTime datetimeField) {
            this.textField = textField;
            this.integerField = integerField;
            this.doubleField = doubleField;
            this.datetimeField = datetimeField;
        }
    }


    @Test
    public void Test() throws Exception {

        List<TestObject> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestObject("abc" + String.valueOf(i),
                    i, i, LocalDateTime.now().plus(i, ChronoUnit.MINUTES)));
        }


        DataProvider<TestObject> dataProvider = (item, fieldName) -> {
            try {
                return TestObject.class.getField(fieldName).get(item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return null;
        };

        IfParser ifp = new IfParser(fieldName ->
        {
            return TestObject.class.getField(fieldName).getType();
        });

        AbstractExpression expression = ifp.parse("textField == \"k'j(\\\"dfks \" ||" +
                "( integerField>2 && doubleField<6)");
        List<TestObject> resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() == 3);

        expression = ifp.parse("(textField == \"abc5\" )");
        resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() == 1);

        expression = ifp.parse("!(textField != \"abc2\" )");
        resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() == 1);

        expression = ifp.parse("(textField == \"abc4\" ) && integerField<=5");
        resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() == 1);

        expression = ifp.parse("((doubleField==4.0) && doubleField<4.5) && integerField<=5");
        resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() == 1);

        String middleTime = ifp.getDateTimeFormatter().format(list.get(5).datetimeField);
        expression = ifp.parse("(datetimeField<\"" + middleTime + "\")|| doubleField>=9.4");
        resultList = getCollect(list, dataProvider, expression);
        assert (resultList.size() > 3);
    }

    private List<TestObject> getCollect(List<TestObject> list, DataProvider<TestObject> dataProvider, AbstractExpression expression) {
        return list.stream().filter(s ->
                expression.testValue(s, dataProvider)).collect(Collectors.toList());
    }


}
