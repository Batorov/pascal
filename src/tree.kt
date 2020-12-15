import java.util.ArrayList
import kotlin.math.exp

abstract class Node

interface NodeVisitor {
    fun visit(node: Node)
}

class Empty(): Node() {
    override fun toString(): String {
        return "Empty"
    }
}

class StatementList(val Statements: MutableList<Node> = ArrayList()): Node() {
    override fun toString(): String {
        return "StatementList $Statements"
    }
}

class Variable(val token: Token): Node() {
    override fun toString(): String {
        return "Variable ($token)"
    }
}

class Assignment(val variable: Node, val expr: Node): Node() {
    override fun toString(): String {
        return "Assignment ($variable, $expr)"
    }
}

class Number(val token: Token): Node() {
    override fun toString(): String {
        return "Number ($token)"
    }
}

class BinOp(val left: Node, val op: Token, val right: Node): Node() {
    override fun toString(): String {
        return "BinOp${op.value} ($left, $right)"
    }
}

class UnaryOp(val op: Token, val expr: Node): Node() {
    override fun toString(): String {
        return "UnaryOp${op.value} ($expr)"
    }
}