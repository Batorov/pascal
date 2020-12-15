
class InterpreterException(message: String): Exception(message)

fun Boolean.ifTrue(action: () -> Unit) {
    if (this) {
        action()
    }
}

class Interpreter(private val verbose: Boolean = false): NodeVisitor {
    private fun print(s: String) = verbose.ifTrue { println(s) }
    var variables = mutableMapOf<String, Double>()

    override fun visit(node: Node) {
        when (node) {
            is Empty -> {}
            is StatementList -> visitStatementList(node)
            is Assignment -> visitAssignment(node)
        }
    }

    fun visitExpr(node: Node): Double {
        when (node) {
            is Number -> return visitNumber(node)
            is BinOp -> return visitBinOp(node)
            is UnaryOp -> return visitUnaryOp(node)
            is Variable -> return visitVariable(node)
        }
        throw InterpreterException("Invalid node")
    }

    private fun visitStatementList(node: Node) {
        node as StatementList;
        print("visit $node")
        for (statement in node.Statements) {
            visit(statement)
        }
    }

    private fun visitAssignment(node: Node) {
        node as Assignment;
        node.variable as Variable
        print("visit $node")
        variables[node.variable.token.value] = visitExpr(node.expr)
    }

    private fun visitVariable(node: Node): Double {
        node as Variable
        print("visit $node")
        return variables[node.token.value]!!
    }

    private fun visitNumber(node: Node): Double {
        node as Number
        print("visit $node")
        return node.token.value.toDouble()
    }

    private fun visitBinOp(node: Node): Double {
        node as BinOp
        print("visit $node")
        when (node.op.type) {
            TokenType.PLUS -> return visitExpr(node.left) + visitExpr(node.right)
            TokenType.MINUS -> return visitExpr(node.left) - visitExpr(node.right)
            TokenType.DIV -> return visitExpr(node.left) / visitExpr(node.right)
            TokenType.MUL -> return visitExpr(node.left) * visitExpr(node.right)
        }
        throw InterpreterException("Invalid operator")
    }

    private fun visitUnaryOp(node: Node): Double {
        node as UnaryOp
        print("visit $node")

        when (node.op.type) {
            TokenType.PLUS -> return +visitExpr(node.expr)
            TokenType.MINUS -> return -visitExpr(node.expr)
        }
        throw InterpreterException("invalid unary")
    }

    fun interpret(tree: Node) {
        visit(tree)
    }
}

fun main() {
    val parser = Parser(Lexer("ex3.txt"))
    val tree = parser.program()
    val interpreter = Interpreter()
    interpreter.interpret(tree)
    println(interpreter.variables)
}