package parser;

import Log.Log;
import scanner.token.Token;

public class ActionHandler {
    private final Parser parser;

    public ActionHandler(Parser parser) {
        this.parser = parser;
    }

    public boolean handleAccept() {
        return true;
    }

    void handleReduce(Action currentAction, Token lookAhead) {
        Rule rule = parser.getRules().get(currentAction.number);
        for (int i = 0; i < rule.RHS.size(); i++) {
            parser.getParsStack().pop();
        }
        Log.print(/*"state : " +*/ parser.getParsStack().peek() + "\t" + rule.LHS);
        parser.getParsStack().push(parser.getParseTable().getGotoTable(parser.getParsStack().peek(), rule.LHS));
        Log.print(/*"new State : " + */parser.getParsStack().peek() + "");
        try {
            parser.getCodeGeneratorFacade().semanticFunction(rule.semanticAction, lookAhead);
        } catch (Exception e) {
            Log.print("Code Genetator Error");
        }
    }

    Token handleShift(Action currentAction) {
        Token lookAhead;
        parser.getParsStack().push(currentAction.number);
        lookAhead = parser.getLexicalAnalyzer().getNextToken();
        return lookAhead;
    }
}