package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String>{
    String Print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    private void parenthesize
}
