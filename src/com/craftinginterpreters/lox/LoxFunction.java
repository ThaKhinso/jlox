package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Expr.AnoFunc declarationAno;
    private final Environment closure;
    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.closure = closure;
        this.declaration = declaration;
        this.declarationAno = null;
    }

    LoxFunction(Expr.AnoFunc declaration, Environment closure) {
        this.declarationAno = declaration;
        this.closure = closure;
        this.declaration = null;
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
        return null;
    }

    @Override
    public int arity() {
        return declaration.params.size();
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
