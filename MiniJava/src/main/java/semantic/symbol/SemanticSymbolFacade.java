package semantic.symbol;

import codeGenerator.Address;
import codeGenerator.Memory;
import codeGenerator.varType;
import types.Tuple;

import static semantic.symbol.SymbolType.Bool;
import static semantic.symbol.SymbolType.Int;

public class SemanticSymbolFacade {
    private SymbolTable symbolTable;
    private Memory memory;

    public SemanticSymbolFacade(Memory memory) {
        symbolTable = new SymbolTable(memory);
        this.memory = memory;
    }

    public void addMethod(String className, String methodName) {
        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());
    }

    public Symbol get(String fieldName, String className) {
        return symbolTable.get(fieldName, className);
    }
    public Tuple<Integer, varType> getAddressAndType(String fieldName, String className) {
        Symbol s = symbolTable.get(fieldName, className);
        return getIntegervarTypeTuple(s);
    }

    public Symbol get(String className, String methodName, String variable) {
        return symbolTable.get(className, methodName, variable);
    }

    public Tuple<Integer, varType> getAddressAndType(String className, String methodName, String variable) {
        Symbol s = symbolTable.get(className, methodName, variable);
        return getIntegervarTypeTuple(s);
    }

    private static Tuple<Integer, varType> getIntegervarTypeTuple(Symbol s) {
        varType t = varType.Int;
        switch (s.type) {
            case Bool:
                t = varType.Bool;
                break;
            case Int:
                t = varType.Int;
                break;
        }
        return new Tuple<>(s.address, t);
    }

    public Address get(String keywordName) {
        return symbolTable.get(keywordName);
    }

    public void startCall(String className, String methodName) {
        symbolTable.startCall(className, methodName);
    }

    private varType getVarTypeOfSymbol(Symbol s) {
        varType t = varType.Int;
        switch (s.type) {
            case Bool:
                t = varType.Bool;
                break;
            case Int:
                t = varType.Int;
                break;
        }
        return t;
    }

    public void setLastType(SymbolType type) {
        symbolTable.setLastType(type);
    }

    public void setLastTypeBool() {
        setLastType(Bool);
    }

    public void setLastTypeInt() {
        setLastType(Int);
    }

    public void addClass(String className) {
        symbolTable.addClass(className);
    }

    public int getMethodReturnAddress(String className, String methodName) {
        return symbolTable.getMethodReturnAddress(className, methodName);
    }

    public varType getMethodReturnType(String className, String methodName) {
        SymbolType t = symbolTable.getMethodReturnType(className, methodName);
        varType temp = varType.Int;
        switch (t) {
            case Int:
                break;
            case Bool:
                temp = varType.Bool;
        }
        return temp;
    }

    public int getMethodAddress(String className, String methodName) {
        return symbolTable.getMethodAddress(className, methodName);
    }

    public Symbol getNextParam(String className, String methodName) {
        return symbolTable.getNextParam(className, methodName);
    }
    public Tuple<Integer, varType> getNextParamAddressAndType(String className, String methodName) {
        Symbol s = symbolTable.getNextParam(className, methodName);
        return getIntegervarTypeTuple(s);
    }

    public void addField(String fieldName, String className) {
        symbolTable.addField(fieldName, className);
    }

    public void addMethod(String className, String methodName, int address) {
        symbolTable.addMethod(className, methodName, address);
    }

    public void addMethodParameter(String className, String methodName, String parameterName) {
        symbolTable.addMethodParameter(className, methodName, parameterName);
    }

    public void addMethodLocalVariable(String className, String methodName, String localVariableName) {
        symbolTable.addMethodLocalVariable(className, methodName, localVariableName);
    }

    public void setSuperClass(String superClass, String className) {
        symbolTable.setSuperClass(superClass, className);
    }

    public int getMethodCallerAddress(String className, String methodName) {
        return symbolTable.getMethodCallerAddress(className, methodName);
    }


}
