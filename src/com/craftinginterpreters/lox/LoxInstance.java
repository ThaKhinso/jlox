package com.craftinginterpreters.lox;

public class LoxInstance {
    private LoxClass klass;
    LoxInstance(LoxClass Class) {
        this.klass = Class;
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
