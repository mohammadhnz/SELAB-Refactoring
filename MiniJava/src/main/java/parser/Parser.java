package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import Log.Log;
import codeGenerator.CodeGeneratorFacade;
import errorHandler.ErrorHandler;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

public class Parser {
    private final ActionHandler actionHandler = new ActionHandler(this);
    private ArrayList<Rule> rules;
    private Stack<Integer> parsStack;
    private ParseTable parseTable;

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public Stack<Integer> getParsStack() {
        return parsStack;
    }

    public ParseTable getParseTable() {
        return parseTable;
    }

    public scanner.lexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public CodeGeneratorFacade getCodeGeneratorFacade() {
        return codeGeneratorFacade;
    }

    private lexicalAnalyzer lexicalAnalyzer;
    private CodeGeneratorFacade codeGeneratorFacade;

    public Parser() {
        parsStack = new Stack<Integer>();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<Rule>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        codeGeneratorFacade = new CodeGeneratorFacade();
    }

    public void startParse(java.util.Scanner sc) {
        lexicalAnalyzer = new lexicalAnalyzer(sc);
        Token lookAhead = lexicalAnalyzer.getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(/*"lookahead : "+*/ lookAhead.toString() + "\t" + parsStack.peek());
                currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);
                Log.print(currentAction.toString());
                if (currentAction.getClass().equals(ShiftAction.class)) {
                    lookAhead = actionHandler.handleShift(currentAction);
                } else if (currentAction.getClass().equals(ReduceAction.class)) {
                    actionHandler.handleReduce(currentAction, lookAhead);
                } else if (currentAction.getClass().equals(AcceptAction.class)) {
                    finish = actionHandler.handleAccept();
                }
                Log.print("");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        if (!ErrorHandler.hasError) codeGeneratorFacade.printMemory();
    }

    private void handleReduce(Action currentAction, Token lookAhead) {
        actionHandler.handleReduce(currentAction, lookAhead);
    }

    private Token handleShift(Action currentAction) {
        return actionHandler.handleShift(currentAction);
    }
}
