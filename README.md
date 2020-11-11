# if-parser
 abbility to parse standard expressions if (expression || expression)
 
 Let's study on Test.java example
 1. Create Entity data object
 
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
	

2. Fill list for testing

     List<TestObject> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestObject("abc" + String.valueOf(i),
                    i, i, LocalDateTime.now().plus(i, ChronoUnit.MINUTES)));
        }


3. Create instance of parser
 IfParser ifp = new IfParser(dataProvider);
 
 dataProvider - up to you - you must create it. Please look at Tests.java
 
4. Now you are ready to create Java representation for expressions like this
integerField >4
doubleField <=4.6
doubleField!=3.4
!(textField != "abc2" )
textField == "k'j(\"dfks \" ||( integerField>2 && doubleField<6)
((doubleField==4.0) && doubleField<4.5) && integerField<=5

This representation decoupled from data, so you can cache it and reuse

5. traverse through your data in synchro or parallel mode
expression = ifp.parse("(textField == \"abc4\" ) && integerField<=5");
list.stream().filter(s ->
                expression.testValue(s, dataProvider))
				.collect(Collectors.toList())


Restrictions:
It does not support Boolean at this time (first RC version)
It does not support expressions like "abc2" != textField  
, but only textField != "abc2"
(left side: variable, right side: static value)

 