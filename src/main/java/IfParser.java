
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfParser {


    /**
     * latitude >= 32.32
     */
    private static final Pattern patternExpression =
            Pattern.compile("(?<left>[a-zA-Z0-9]+)[ ]*(?<op>(<=)|(>=)|(>)|(<)|(==)|(!=))[ ]*(?<right>.+)");
    private static final Pattern patternBraces = Pattern.compile("(^[(])|([)]$)");
    private static final String quoteRemover = "(^[\"])|([\"]$)";

    private DataProvider dataProvider;

    public IfParser(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public AbstractExpression parse(String whereClause) throws Exception {
        whereClause = normalize(whereClause);
        char[] chars = whereClause.toCharArray();

        Stack<CompositeExpression> stack = new Stack<>();
        CompositeExpression currentExpression = new CompositeExpression(false);
        StringBuilderDecorator textBuilder = new StringBuilderDecorator(chars.length);

        boolean insideTextPart = false;
        boolean insideExpression = false;
        boolean escapeCharacter = false; //it was \
        int logicCharacter = 0; //it was first &| of &&||
        boolean notFlag = false; // ! before (

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (insideTextPart) {
                textBuilder.append(c, false);
                if (isQuote(c) && !escapeCharacter) {
                    insideTextPart = false;
                } else if (isEscape(c)) {
                    escapeCharacter = true;
                } else {
                    escapeCharacter = false;
                }
            } else if (isQuote(c)) {
                textBuilder.append(c, false);
                insideTextPart = true;
            } else if (isOpenBracet(c)) {
                insideExpression = false;
                logicCharacter=0;
                stack.push(currentExpression);
                currentExpression = new CompositeExpression(notFlag);
                notFlag = false;
            } else if (isCloseBracet(c)) {
                insideExpression = false;
                logicCharacter=0;
                if (!textBuilder.isEmpty()) {
                    currentExpression.addFlat(getExpression(textBuilder.toString()));
                    textBuilder.reset();
                }
                CompositeExpression previous = stack.pop();
                previous.addFlat(currentExpression);
                currentExpression = previous;
            } else if (isLogicChar(c)) {
                insideExpression = false;
                if (++logicCharacter > 2) {
                    throw new ParseException("logic character x3");
                }
                if (logicCharacter == 1) {
                    if (!textBuilder.isEmpty()) {
                        currentExpression.addFlat(getExpression(textBuilder.toString()));
                        textBuilder.reset();
                    }
                    currentExpression.addLogic(c);
                }
            } else if (isNot(c) && !insideExpression){
                // ! before ( or boolean
                // not a != operator
                notFlag = true;
            }
            else {
                logicCharacter = 0;
                if (textBuilder.append(c, true))
                {
                    insideExpression = true;
                }
            }
        }

        if (!textBuilder.isEmpty()) {
            currentExpression.addFlat(getExpression(textBuilder.toString()));
        }

        return currentExpression;
    }


    /**
     * @param leafClause must contain one of  ==, != , >, <, >=, <=
     * @return
     * @throws ParseException
     */
    private AbstractExpression getExpression(String leafClause) throws ParseException {
        Matcher m = patternExpression.matcher(leafClause);
        if (m.find()) {
            String left = m.group("left");
            String right = m.group("right");
            ExpressionTypes exp = ExpressionTypes.parse(m.group("op"));
            Type type = dataProvider.getTypeOrNull(left);
            //TODO: переворачиваем если аргументы перепутаны
            if (type == Integer.class || type == int.class) {
                return new IntegerExpression(left, Integer.parseInt(right), exp);
            } else if (type == Double.class || type == double.class ||
                    type == Float.class || type == float.class) {
                return new DoubleExpression(left, Double.parseDouble(right), exp);
            } else if (type == LocalDateTime.class) {
                right = right.trim().replaceAll(quoteRemover,"");
                LocalDateTime rightValue = LocalDateTime.parse(right, dataProvider.getDateTimeFormatter());
                return new DateTimeExpression(left, rightValue, exp);
            } else if (type == String.class) {
                right = right.trim().replaceAll(quoteRemover,"");
                return new StringExpression(left, right, exp);
            }
        }
        throw new ParseException(leafClause);
    }

    private static boolean isOpenBracet(char c) {
        return c == '(';
    }


    private static boolean isCloseBracet(char c) {
        return c == ')';
    }

    private static boolean isQuote(char c) {
        return c == '"';
    }

    private static boolean isEscape(char c) {
        return c == '\\';
    }

    private static boolean isLogicChar(char c) {
        return c == '&' || c == '|';
    }

    private static boolean isNot(char c) {
        return c == '!';
    }

    private static String normalize(String whereClause) {
        return whereClause.trim();
    }

    private class StringBuilderDecorator {
        private final static char whitespace = ' ';
        private StringBuilder stringBuilder;

        StringBuilderDecorator(int capacity) {
            stringBuilder = new StringBuilder(capacity);
        }


        boolean append(char c, boolean skipWhiteSpace) {
            if (skipWhiteSpace && c == whitespace) {
                return false;
            }
            stringBuilder.append(c);
            return true;
        }

        boolean isEmpty() {
            return stringBuilder.length() == 0;
        }

        void reset() {
            stringBuilder.setLength(0);
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}
