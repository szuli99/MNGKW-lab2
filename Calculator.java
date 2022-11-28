import java.util.LinkedList;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Calculator extends CalculatorBaseListener {
    private final LinkedList<Double> firstStack = new LinkedList<>();
    private final LinkedList<Double> secondStack = new LinkedList<>();
    private final LinkedList<Double> thirdStack = new LinkedList<>();

    public Double getResult() {
        return thirdStack.pop();
    }

    @Override
    public void exitExpression(CalculatorParser.ExpressionContext ctx) {
        Double result = thirdStack.removeLast();
        for (int i = 1; i < ctx.getChildCount(); i = i + 2) {
            if (symbolEquals(ctx.getChild(i), CalculatorParser.PLUS)) {
                result = result + thirdStack.removeLast();
            } else {
                result = result - thirdStack.removeLast();
            }
        }
        thirdStack.push(result);
        System.out.println("Expression: \"" + ctx.getText() + "\" -> " + result);
    }

    @Override
    public void exitMultiplyingExpression(CalculatorParser.MultiplyingExpressionContext ctx) {
        Double result = secondStack.removeLast();
        for (int i = 1; i < ctx.getChildCount(); i = i + 2) {
            if (symbolEquals(ctx.getChild(i), CalculatorParser.TIMES)) {
                result = result * secondStack.removeLast();
            } else {
                result = result / secondStack.removeLast();
            }
        }
        thirdStack.push(result);
        System.out.println("MultiplyingExpression: \"" + ctx.getText() + "\" -> " + result);
    }

    private boolean symbolEquals(ParseTree child, int symbol) {
        return ((TerminalNode) child).getSymbol().getType() == symbol;
    }

    @Override public void exitPowExpression(CalculatorParser.PowExpressionContext ctx) {
        Double result = firstStack.removeLast();
        Double x=0.0;
        for (int i = 1; i < ctx.getChildCount(); i = i + 2) {
            if (symbolEquals(ctx.getChild(i), CalculatorParser.POW)) {
                result = Math.pow(result,firstStack.removeLast());
            }
            else if (symbolEquals(ctx.getChild(i), CalculatorParser.SQRT)) {
                result = Math.pow(result,1/firstStack.removeLast());
            }
        }
        secondStack.push(result);
        System.out.println("powExpression: \"" + ctx.getText() + "\" -> " + result);
    }
    @Override public void exitFloatExpresion(CalculatorParser.FloatExpresionContext ctx) {
        Double value = Double.valueOf(ctx.FLOAT().getText());
        if (ctx.FLOAT() != null) {
            if(ctx.MINUS() != null){
                firstStack.push(-1 * value);
            }
            else
            {
                firstStack.push(value) ;
            }
        }
        System.out.println("floatExpression: \"" + ctx.getText() + "\" ");
    }

    public static Double calc(CharStream charStream) {
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        System.out.println(tokens.getText());

        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.expression();

        ParseTreeWalker walker = new ParseTreeWalker();
        Calculator calculatorListener = new Calculator();
        walker.walk(calculatorListener, tree);
        return calculatorListener.getResult();
    }

    public static Double calc(String expression) {
        return calc(CharStreams.fromString(expression));
    }

    public static void main(String[] args) throws Exception {
        CharStream charStreams = CharStreams.fromFileName("./example.txt");
        Double result = calc(charStreams);
        System.out.println("Result = " + result);
    }
}
