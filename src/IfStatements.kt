import java.util.*

class IfStatements(var leftVariable : Any, var rightVariable : Any, var operator : String,var interpreter : Interpreter) : Commands(){

    private var condition : Boolean = false
    override fun begin(){
        checkCondition()
        println(condition)
        for(event in queue){
            if(condition)
                interpreter.process(event)
        }
    }


    fun checkCondition() {
        when (operator) {
            "<" -> {
                if (leftVariable is Int && rightVariable is Int)
                    condition = ((leftVariable as Int) < (rightVariable as Int))
                else
                    throw SyntaxError("Syntax error", Throwable())
            }
            ">" -> {
                if (leftVariable is Int && rightVariable is Int)
                    condition = ((leftVariable as Int) > (rightVariable as Int))
                else
                    throw SyntaxError("Syntax error", Throwable())
            }
            "==" -> {
                if (leftVariable is Int && rightVariable is Int)
                    condition = ((leftVariable as Int) == (rightVariable as Int))
                else if (leftVariable is String && rightVariable is String) {
                    condition = ((leftVariable as String)) == (rightVariable as String)
                } else
                    throw SyntaxError("Syntax error", Throwable())
            }
            "!=" -> {
                if (leftVariable is Int && rightVariable is Int)
                    condition = ((leftVariable as Int) != (rightVariable as Int))
                else if (leftVariable is String && rightVariable is String) {
                    condition = ((leftVariable as String)) != (rightVariable as String)
                } else
                    throw SyntaxError("Syntax error", Throwable())
            }
            else -> throw SyntaxError("Syntax Error", Throwable())
        }
    }

}