package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Expr.AnoFunc declarationAno;
    private final Environment closure;
    private final boolean isInitializer;
    private final boolean isStaticMethod;
    LoxFunction(Stmt.Function declaration, Environment closure,
                boolean isInitializer, boolean isStaticMethod) {
        this.closure = closure;
        this.declaration = declaration;
        this.declarationAno = null;
        this.isInitializer = isInitializer;
        this.isStaticMethod = isStaticMethod;
    }

    LoxFunction(Expr.AnoFunc declaration, Environment closure,
                boolean isInitializer) {
        this.declarationAno = declaration;
        this.closure = closure;
        this.declaration = null;
        this.isInitializer = isInitializer;
        this.isStaticMethod = false;
    }

    LoxFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        if (isStaticMethod) return new LoxFunction(declaration, environment, isInitializer, isStaticMethod);
        environment.define("this", instance);
        return new LoxFunction(declaration, environment, isInitializer, isStaticMethod);
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        List<Token> paramsb;
        List<Stmt> body;
        if (declaration != null) {
            paramsb = declaration.params;
            body = declaration.body;
        } else {
            paramsb = declarationAno.params;
            body = declarationAno.body;
        }
        for (int i = 0; i < paramsb.size(); i++) {
            environment.define(paramsb.get(i).lexeme,
                    arguments.get(i));
        }
        try {
            interpreter.executeblock(body, environment);
        } catch (Return returnvalue) {
            return returnvalue.value;
        }
        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }

    @Override
    public int arity() {
        if (declaration != null) {
            return declaration.params.size();
        } else  {
            return declarationAno.params.size();
        }
    }

    @Override
    public String toString() {
        if (declaration != null) {
            return "<fn " + declaration.name.lexeme + ">";
        } else {
            return "<fn>";
        }
    }
}
