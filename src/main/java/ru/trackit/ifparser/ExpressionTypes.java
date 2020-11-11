package ru.trackit.ifparser;

public enum ExpressionTypes {
    More(">"),
    MoreOrEqual(">="),
    Less("<"),
    LessOrEqual("<="),
    Equal("=="),
    NotEqual("!=");

    final String text;
    ExpressionTypes(String text){
        this.text=text;
    }

    public static ExpressionTypes parse(String text) throws ParseException {
        for (ExpressionTypes exp : values()){
            if (exp.text.equalsIgnoreCase(text)){
                return exp;
            }
        }
        throw new ParseException(text);
    }
}
