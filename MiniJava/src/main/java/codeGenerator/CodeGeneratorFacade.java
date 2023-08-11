package codeGenerator;

import scanner.token.Token;

public class CodeGeneratorFacade {
    CodeGenerator codeGenerator;

    public CodeGeneratorFacade() {
        codeGenerator = new CodeGenerator();
    }

    public void printMemory() {
        codeGenerator.printMemory();
    }

    public void semanticFunction(int func, Token next) {
        codeGenerator.semanticFunction(func, next);
    }


}
