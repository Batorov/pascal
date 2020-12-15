import java.io.File

class Lexer(val path: String) {
    private var pos: Int = 0
    private var text = File(path).readLines().joinToString("\n");
    private var currentChar: Char? = null

    init {
        currentChar = text[pos]
    }


    public fun nextToken(): Token {
        var value: String
        var type: TokenType?
        while (currentChar != null) {
            if (currentChar!!.isWhitespace()) {
                skip()
                continue
            }
            if (currentChar!!.isDigit()) {
                return Token(TokenType.NUMBER, number())
            }
            if (currentChar!!.isLetter()) {
                val word = word();
                if (word == "BEGIN")
                    return Token(TokenType.BEGIN, word)
                if (word == "END")
                    return Token(TokenType.END, word)

                return Token(TokenType.VAR, word)
            }
            if (currentChar!! == ':') {
                forward();
                if (currentChar == '=')
                    forward();
                    return Token(TokenType.ASSIGNMENT, ":=")
            }
            type = null
            value = "$currentChar"

            when (currentChar) {
                '+' -> type = TokenType.PLUS
                '-' -> type = TokenType.MINUS
                '/' -> type = TokenType.DIV
                '*' -> type = TokenType.MUL
                '(' -> type = TokenType.LPAREN
                ')' -> type = TokenType.RPAREN
                '.' -> type = TokenType.DOT
                ';' -> type = TokenType.SEMICOLON
            }

            type?.let {
                forward()
                return Token(it, value)
            }
            /*if (type != null) {
                forward()
                return Token(type, value)
            }*/

            /*
            if (currentChar == '+') {
                forward()
                return Token(TokenType.PLUS, "$currentChar")
            }
            if (currentChar == '-') {
                forward()
                return Token(TokenType.MINUS, "$currentChar")
            }
            if (currentChar == '*') {
                forward()
                return Token(TokenType.MUL, "$currentChar")
            }
            if (currentChar == '/') {
                forward()
                return Token(TokenType.DIV, "$currentChar")
            }
            if (currentChar == '(') {
                forward()
                return Token(TokenType.LPAREN, "$currentChar")
            }
            if (currentChar == ')') {
                forward()
                return Token(TokenType.RPAREN, "$currentChar")
            }*/
            throw InterpreterException("invalid token")
        }
        return Token(TokenType.EOL, "")
    }

    private fun forward() {
        pos += 1
        if (pos > text.length - 1) {
            currentChar = null
        }
        else {
            currentChar = text[pos]
        }
    }

    private fun skip() {
        while ((currentChar != null) && currentChar!!.isWhitespace()){
            forward()
        }
    }

    private fun number(): String {
        var result = arrayListOf<Char>()
        while ((currentChar != null) && ((currentChar!!.isDigit()) || (currentChar == '.'))) {
            result.add(currentChar!!)

            forward()
        }
        return result.joinToString("")
    }
    private fun word(): String {
        var result = arrayListOf<Char>()
        while ((currentChar != null) && currentChar!!.isLetter()) {
            result.add(currentChar!!)
            forward()
        }
        return result.joinToString("")
    }

}

fun main(args: Array<String>) {
    val lexer = Lexer("ex1.txt")
    println(lexer.nextToken())
    println(lexer.nextToken())

}
