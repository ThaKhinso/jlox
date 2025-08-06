package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String>{
    String Print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return "";
    }


    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return "";
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return "";
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "";
    }


    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitAnoFuncExpr(Expr.AnoFunc expr) {
        return "";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr: exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(46.57)
                )
        );
        System.out.println(new AstPrinter().Print(expression));
    }
}

class RPNPrinter implements Expr.Visitor<String>{
    String Print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAnoFuncExpr(Expr.AnoFunc expr) {
        return "";
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return "";
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return "";
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return "";
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "";
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return expr.left.accept(this) + " " +
               expr.right.accept(this) + " " +
               expr.operator.lexeme;
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return expr.right.accept(this) + " " +
               expr.operator.lexeme ;
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr: exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(46.57)
                )
        );
        if (!!!true) {
            System.out.println("test");
        }
        System.out.println(new RPNPrinter().Print(expression));
    }
}