import java.awt.EventQueue
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap




class SyntaxError(var errorMsg : String, var trw : Throwable) : Exception(errorMsg,trw)
class VariableNotDeclared(var errorMsg : String, var trw : Throwable) : Exception(errorMsg,trw)

class Interpreter(){
    private val frame : View = View("Simple Interpreter",this)
    private val eventStack = Stack<Commands>()
    init{
        frame.isVisible = true
    }
    private var intVariables = HashMap<String,Int>()
    private var stringVariables = HashMap<String,String>()
    private var boolVariables = HashMap<String, Boolean>()
    val KEYWORDS : Array<String> = arrayOf("for","if","str","int","boolean","say")

    fun process(text : String){
        when(text.split(" ")[0]){
            "if" ->{ifStatement(text)}
            "for" ->{forLoops(text)}
            "int" ->{addVariable(text)}
            "str" ->{addVariable(text)}
            "boolean" ->{addVariable(text)}
            "say" ->{
                if(eventStack.isEmpty()) say(text)
                else eventStack.peek().queue.push(text)
            }
            "}" ->{
                frame.tabs--
                frame.indent = ""
                for(i in 1..frame.tabs){
                    frame.indent += "   "
                }
                    if(eventStack.size > 1){
                        var f = eventStack.pop()
                        var n = eventStack.peek()
                        for(processes in f.queue)
                            for(i in f.start..f.end)
                                n.queue.add(processes)
                    }
                    else eventStack.pop().begin()
                }

            else ->{if(eventStack.isEmpty()) reassignment(text)
            else eventStack.peek().queue.push(text)}
        }

    }

    private fun getIntValue(text : String) : Int{
        if(text.matches(Regex("-?\\d+")))
            return text.toInt()
        else if(intVariables.containsKey(text))
            return intVariables[text]!!

        throw SyntaxError("Syntax Error", Throwable())
    }

    private fun getStringValue(text: String) : String{
        if(stringVariables.containsKey(text)){
            return stringVariables[text]!!
        }
        throw SyntaxError("Syntax Error", Throwable())
    }

    private fun reassignment(text : String){
        if(text.split(" ")[1] != "=")
            throw SyntaxError("Syntax error", Throwable())
        if(stringVariables.containsKey(text.split(" ")[0]))
            stringVariables[text.split(" ")[0]] = getVariableValue("str $text").toString()
        else if(intVariables.containsKey(text.split(" ")[0]))
            intVariables[text.split(" ")[0]] = processOperations(text.split("=")[1].replaceFirst(" ",""))
        else if(boolVariables.containsKey(text.split(" ")[0]))
            boolVariables[text.split(" ")[0]] = (text.split(" ")[2] == "true")
        else
            throw VariableNotDeclared("Error variable ${text.split(" ")[0]} has not been declared",Throwable())
    }



    private fun ifStatement(text : String){
        var v : IfStatements? = null
        if(text.split(" ")[1] == "true") {
            v = IfStatements(1, 1, "==", this)
        }
        else if(text.split(" ")[1] == "false")
            v = IfStatements(1,2,"==",this)
        else if (boolVariables.containsKey(text.split(" ")[1])){
            if(boolVariables[text.split(" ")[1]]!!)
                v = IfStatements(1,1,"==",this)
            else
                v = IfStatements(1,2,"==",this)
        }
        else {
            var left : Any
            var right : Any
            try {
                left  = getIntValue(text.split(" ")[1])
                right = getIntValue(text.split(" ")[3])
            }
            catch(e : SyntaxError){
                left = getStringValue(text.split(" ")[1])
                right = getStringValue(text.split(" ")[3])
            }
                v = IfStatements(left, right, text.split(" ")[2], this)
        }
        frame.indent += "   "
        frame.tabs++
        eventStack.push(v)

    }
    private fun processOperations(text: String): Int {
        var operationStack = Stack<Int>()
        var rpn = convertToRPN(text).replaceFirst(" ","")
        println(rpn)
        for (words in rpn.split(" ")) {
            try {
                when (words) {
                    "+" -> {
                        operationStack.push(operationStack.pop() + operationStack.pop())
                    }
                    "-" -> {
                        operationStack.push(operationStack.pop() - operationStack.pop())
                    }
                    "/" -> {
                        operationStack.push(operationStack.pop() / operationStack.pop())
                    }
                    "*" -> {
                        operationStack.push(operationStack.pop() * operationStack.pop())
                    }
                    else -> {
                        println(words.matches(Regex("-?\\d+")))
                        if (intVariables.containsKey(words))
                            operationStack.push(intVariables[words])

                        else if (words.matches(Regex("-?\\d+"))) {
                            operationStack.push(words.toInt())
                        }
                        else {
                            throw SyntaxError("Syntax Error",Throwable())
                        }
                    }
                }
            } catch (e: Throwable) {
                throw SyntaxError("Syntax error",Throwable())
            }
        }

        return operationStack.peek()
    }


    private fun forLoops(text : String){
        val start = text.split(" ")[1].toInt()
        val end = text.split(" ")[3].toInt()
        require(text.split(" ")[2] == "in") { throw SyntaxError("Syntax Error", Throwable()) }
        require(text.split(" ")[4] == "{"){ throw SyntaxError("Syntax Error",Throwable())}
        var f = ForLoops(start,end,this)
        frame.indent += "   "
        frame.tabs++
        eventStack.push(f)
    }

    private fun say(text : String){
        if(stringVariables.containsKey(getVariableName(text)))
            frame.say(stringVariables[getVariableName(text)].toString())
        else if(intVariables.containsKey(getVariableName(text)))
            frame.say(intVariables[getVariableName(text)].toString())
        else if(boolVariables.containsKey(getVariableName(text)))
            frame.say(boolVariables[getVariableName(text)].toString())
        else if(text.contains("\""))
            frame.say(getVariableValue(text = (text.replaceFirst("say","str temp = "))) as String)
        else
            throw VariableNotDeclared("Error variable ${getVariableName(text)} has not been declared",Throwable())
    }

    private fun addVariable(text : String){
        when(text.split(" ")[0]){
            "int" ->{intVariables[getVariableName(text)] = (processOperations(text.split("=")[1].replaceFirst(" ","")) as Int)}
            "str" ->{stringVariables[getVariableName(text)] = getVariableValue(text).toString()}
            "boolean" ->{boolVariables[getVariableName(text)] = (getVariableValue(text) as Boolean)}
        }
    }

    private fun getVariableName(text : String) : String{
        require(!text.split(" ")[1].matches(Regex("[a-zA-Z ]*\\d+.*"))) {  throw SyntaxError("Syntax Error",Throwable()) }
        return text.split(" ")[1]
    }

    private fun getVariableValue(text : String) : Any? {
        when(text.split(" ")[0]){
            "int" ->{
                return text.split(" ")[3].toInt()
            }
            "str" -> {
                var textChange = text
                if (textChange.count { "\"".contains(it) } > 2) {
                    throw SyntaxError("Syntax Error",Throwable())
                }
                var start = textChange.indexOf("\"")
                textChange = textChange.replaceFirst("\"", "")
                var end = textChange.indexOf("\"")

                return textChange.substring(start, end)
            }
            "boolean"->{
                if(text.split(" ")[3] != "true" && text.split(" ")[3] != "false")
                    throw SyntaxError("Syntax Error",Throwable())
                else return text.split(" ")[3] == "true"
            }
        }
        return null
    }

    private fun convertToRPN(text:String) : String{
        val prededence = HashMap<String, Int>()
        prededence["/"] = 5
        prededence["*"] = 5
        prededence["+"] = 4
        prededence["-"] = 4

        val queue = LinkedList<String>()
        val stack = Stack<String>()
        for(o in text.split(" ")){

            if(o.matches(Regex("-?\\d+")))
                queue.push(o)
            if (intVariables.containsKey(o)){
                queue.push(intVariables[o].toString())
            }
            else if(prededence.containsKey(o)){
                while(stack.isNotEmpty() && (prededence[stack.peek()]!! >= prededence[o]!!)){
                    queue.push(stack.pop())
                }
                stack.push(o)
            }
        }
        var str = ""
        while(stack.isNotEmpty())
            queue.push(stack.pop())
        while(queue.isNotEmpty())
            str +=queue.pop()+" "

        var s = Stack<String>()
        str.substring(0, str.length-1)
        for(x in str.split(" "))
            s.push(x)
        var temp = ""

        while(s.isNotEmpty())
            temp += " "+s.pop()

        var reg = Regex("^\\s+")
        temp.replace(reg,"")
        return "${temp.replaceFirst(" ","")}"
    }
}

private fun createAndShowGUI(){
    var interpreter = Interpreter()
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}