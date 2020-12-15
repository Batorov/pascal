class Parser(private val lexer: Lexer) {
    private var currentToken: Token = lexer.nextToken()

    private fun checkTokenType(type: TokenType){
        if (currentToken.type == type) {
            currentToken = lexer.nextToken()
        }
        else {
            throw InterpreterException("invalid token order")
        }
    }

    private fun factor(): Node {
        val token = currentToken;
        when (token.type) {
            TokenType.PLUS -> {
                checkTokenType(TokenType.PLUS)
                return UnaryOp(token, factor())
            }
            TokenType.MINUS -> {
                checkTokenType(TokenType.MINUS)
                return UnaryOp(token, factor())
            }
            TokenType.NUMBER -> {
                checkTokenType(TokenType.NUMBER)
                return Number(token)
            }
            TokenType.LPAREN -> {
                checkTokenType(TokenType.LPAREN)
                val result = expr()
                checkTokenType(TokenType.RPAREN)
                return result
            }
            TokenType.VAR -> {
                checkTokenType(TokenType.VAR)
                return Variable(token)
            }
        }
        throw  InterpreterException("invalid factor")
    }

    private fun term(): Node {
        var result = factor()
        val ops = arrayListOf<TokenType>(TokenType.DIV, TokenType.MUL)
        while (ops.contains(currentToken.type)) {
            val token = currentToken
            when (token.type) {
                TokenType.DIV -> {
                    checkTokenType(TokenType.DIV)
                }
                TokenType.MUL -> {
                    checkTokenType(TokenType.MUL)

                }
            }
            result = BinOp(result, token, factor())
        }
        return result
    }

    fun expr(): Node {
        val ops = arrayListOf<TokenType>(TokenType.PLUS, TokenType.MINUS)
        var result = term()
        while (ops.contains(currentToken.type)) {
            val token = currentToken
            if (token.type == TokenType.PLUS) {
                checkTokenType(TokenType.PLUS)
            }
            if (token.type == TokenType.MINUS) {
                checkTokenType(TokenType.MINUS)
            }
            result = BinOp(result, token, term())
        }
        return result
        /*val left = currentToken
        checkTokenType(TokenType.INTEGER)
        val op = currentToken
        if (op.type == TokenType.PLUS) {
            checkTokenType(TokenType.PLUS)
        }else {
            checkTokenType(TokenType.MINUS)
        }
        val right = currentToken
        checkTokenType(TokenType.INTEGER)
        if (op.type == TokenType.PLUS) {
            return left.value.toInt() + right.value.toInt()
        }
        else {
            return left.value.toInt() - right.value.toInt()
        }*/
    }

    fun program(): Node {
        val result = complex_statement();
        val token = currentToken;
        if (token.type == TokenType.DOT) {
            checkTokenType(TokenType.DOT)
            return result
        }
        return result

//        throw  InterpreterException("invalid syntax")
    }

    fun complex_statement(): Node {
        val token = currentToken;
        when (token.type) {
            TokenType.BEGIN -> {
                checkTokenType(TokenType.BEGIN)
                val result = statement_list()
                return result
            }
        }
        throw  InterpreterException("invalid syntax")
    }
    fun statement_list(): Node {
        var result = StatementList()
        val SL:MutableList<Node> = ArrayList();
            while (currentToken.type != TokenType.END && currentToken.type != TokenType.DOT) {
                val st = statement()
                SL.add(st)
                result = StatementList(SL)
                if (currentToken.type == TokenType.SEMICOLON) {
                    checkTokenType(TokenType.SEMICOLON)
                }
            }
            val token = currentToken.type
            checkTokenType(token)
        return result;
    }

    fun statement(): Node {
        var result = empty();
        if (currentToken.type == TokenType.BEGIN) {
            checkTokenType(TokenType.BEGIN)
            return statement_list()
        }
        if (currentToken.type == TokenType.SEMICOLON) {
            checkTokenType(TokenType.SEMICOLON)
            return result
        }
        result = assignment()
        return result
    }
    fun assignment(): Node {
        val variable = variable()
        checkTokenType(TokenType.ASSIGNMENT)
        return Assignment(variable, expr())
    }
    fun variable(): Node {
        //println(currentToken)
        val result = factor()
        return result
    }

    fun empty(): Node {
        return Empty()
    }
}

fun main() {
    val parser = Parser(Lexer("ex3.txt"))
    println(parser.program())
}