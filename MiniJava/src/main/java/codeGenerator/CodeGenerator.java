package codeGenerator;

import Log.Log;
import errorHandler.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.SemanticSymbolFacade;
import types.Tuple;

import java.util.Stack;

public class CodeGenerator {
    private Memory memory = new Memory();
    private Stack<Address> ss = new Stack<Address>();
    private Stack<String> symbolStack = new Stack<>();
    private Stack<String> callStack = new Stack<>();
    private SemanticSymbolFacade semanticSymbolFacade;

    public CodeGenerator() {
        semanticSymbolFacade = new SemanticSymbolFacade(memory);
    }

    public void printMemory() {
        memory.pintCodeBlock();
    }

    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                add();
                break;
            case 11:
                sub();
                break;
            case 12:
                mult();
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                _while();
                break;
            case 16:
                jpf_save();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                less_than();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                popClass();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                semanticSymbolFacade.setLastTypeBool();
                break;
            case 32:
                semanticSymbolFacade.setLastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    private void defMain() {
        //ss.pop();
        memory.add3AddressCode(ss.pop().num, Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        semanticSymbolFacade.addMethod(className, methodName);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }


    public void checkID() {
        symbolStack.pop();
        if (ss.peek().varType == varType.Non) {
            //TODO : error
        }
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {
                Tuple<Integer, varType> result = semanticSymbolFacade.getAddressAndType(className, methodName, next.value);
                ss.push(new Address(result.item1, result.item2));
            } catch (Exception e) {
                ss.push(new Address(0, varType.Non));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    public void fpid() {
        ss.pop();
        ss.pop();

        Tuple<Integer, varType> result = semanticSymbolFacade.getAddressAndType(symbolStack.pop(), symbolStack.pop());
        ss.push(new Address(result.item1, result.item2));

    }

    public void kpid(Token next) {
        ss.push(semanticSymbolFacade.get(next.value));
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        //TODO: method ok
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        semanticSymbolFacade.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);

        //symbolStack.push(methodName);
    }

    public void call() {
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            semanticSymbolFacade.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        varType t = semanticSymbolFacade.getMethodReturnType(className, methodName);
        Address temp = new Address(memory.getTemp(), t);
        memory.moveTempIndex();
        ss.push(temp);
        memory.add3AddressCode(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate), new Address(semanticSymbolFacade.getMethodReturnAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.ASSIGN, new Address(memory.getCurrentCodeBlockAddress() + 2, varType.Address, TypeAddress.Imidiate), new Address(semanticSymbolFacade.getMethodCallerAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.JP, new Address(semanticSymbolFacade.getMethodAddress(className, methodName), varType.Address), null, null);
    }

    public void arg() {
        String methodName = callStack.pop();
        try {
            Tuple<Integer, varType> result = semanticSymbolFacade.getNextParamAddressAndType(callStack.peek(), methodName);
            Address param = ss.pop();
            printErrorMessage(param.varType != result.item2, "The argument type isn't match");
            memory.add3AddressCode(Operation.ASSIGN, param, new Address(result.item1, result.item2), null);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);

    }

    public void assign() {
        Address s1 = ss.pop();
        Address s2 = ss.pop();
        printErrorMessage(s1.varType != s2.varType, "The type of operands in assign is different ");
        memory.add3AddressCode(Operation.ASSIGN, s1, s2, null);
    }

    public void add() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();

        printErrorMessage(s1.varType != varType.Int || s2.varType != varType.Int, "In add two operands must be integer");
        memory.add3AddressCode(Operation.ADD, s1, s2, temp);
        ss.push(temp);
    }

    public void sub() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(
                s1.varType != varType.Int || s2.varType != varType.Int,
                "In sub two operands must be integer"
        );
        memory.add3AddressCode(Operation.SUB, s1, s2, temp);
        ss.push(temp);
    }

    private static void printErrorMessage(boolean varType, String msg) {
        if (varType) {
            ErrorHandler.printError(msg);
        }
    }

    public void mult() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(
                s1.varType != varType.Int || s2.varType != varType.Int,
                "In mult two operands must be integer"
        );
        memory.add3AddressCode(Operation.MULT, s1, s2, temp);
        ss.push(temp);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), varType.Address));
    }

    public void save() {
        ss.push(new Address(memory.saveMemory(), varType.Address));
    }

    public void _while() {
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress() + 1, varType.Address), null);
        memory.add3AddressCode(Operation.JP, ss.pop(), null, null);
    }

    public void jpf_save() {
        Address save = new Address(memory.saveMemory(), varType.Address);
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null);
        ss.push(save);
    }

    public void jpHere() {
        memory.add3AddressCode(ss.pop().num, Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
    }

    public void print() {
        memory.add3AddressCode(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(s1.varType != s2.varType, "The type of operands in equal operator is different");
        memory.add3AddressCode(Operation.EQ, s1, s2, temp);
        ss.push(temp);
    }

    public void less_than() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(s1.varType != varType.Int || s2.varType != varType.Int, "The type of operands in less than operator is different");
        memory.add3AddressCode(Operation.LT, s1, s2, temp);
        ss.push(temp);
    }

    public void and() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(s1.varType != varType.Bool || s2.varType != varType.Bool, "In and operator the operands must be boolean");
        memory.add3AddressCode(Operation.AND, s1, s2, temp);
        ss.push(temp);
    }

    public void not() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.moveTempIndex();
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        printErrorMessage(s1.varType != varType.Bool, "In not operator the operand must be boolean");
        memory.add3AddressCode(Operation.NOT, s1, s2, temp);
        ss.push(temp);
    }

    public void defClass() {
        ss.pop();
        semanticSymbolFacade.addClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        semanticSymbolFacade.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());
        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        semanticSymbolFacade.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        semanticSymbolFacade.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        ss.pop();
        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        semanticSymbolFacade.addMethodLocalVariable(className, methodName, var);
        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        String methodName = symbolStack.pop();
        Address s = ss.pop();
        varType temp = semanticSymbolFacade.getMethodReturnType(symbolStack.peek(), methodName);
        printErrorMessage(s.varType != temp, "The type of method and return address was not match");
        memory.add3AddressCode(Operation.ASSIGN, s, new Address(semanticSymbolFacade.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address, TypeAddress.Indirect), null);
        memory.add3AddressCode(Operation.JP, new Address(semanticSymbolFacade.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address), null, null);
    }

    public void defParam() {
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        semanticSymbolFacade.addMethodParameter(className, methodName, param);
        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void main() {

    }
}
