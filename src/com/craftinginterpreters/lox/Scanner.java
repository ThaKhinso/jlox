package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;
import static com.craftinginterpreters.lox.TokenType.*;
import com.craftinginterpreters.lox.lox.*;
public class Scanner {
    private final String source;
    private final List<Token>tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            // we are at the beginning of the next lexme.
            start = current;
            ScanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void ScanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : ASSIGNMENT);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS );
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                }else {
                    lox.error(line, "Unexpected Error");
                }
                break;
        }
    }

    boolean isAlpha(char c) {
        return ( c >= 'a' && c <= 'z') ||
               ( c >= 'A' && c <= 'Z') ||
               ( c == '_');
    }

    boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(start,current)) );
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current+1);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            lox.error(line, "Unterminated String");
            return;
        }

        advance();

        String value = source.substring(start+1, current-1);
        addToken(STRING, value);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
