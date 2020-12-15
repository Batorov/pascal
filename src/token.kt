enum class TokenType {
    NUMBER,
    PLUS,
    MINUS,
    DIV,
    MUL,
    LPAREN,
    RPAREN,
    EOL,
    DOT,
    SEMICOLON,
    BEGIN,
    END,
    VAR,
    ASSIGNMENT
}

class Token(val type: TokenType, val value: String) {

    override fun toString(): String {
        return "Token ($type, $value)"
    }
}