fun main(args: Array<String>) {
    val parser = Parser(Lexer("ex3.txt"))
    val tree = parser.program()
    val interpreter = Interpreter()
    interpreter.interpret(tree)
    println(interpreter.variables)
}