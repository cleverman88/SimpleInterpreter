import java.awt.EventQueue
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap




class Interpreter(){
    private val frame : View = View("Simple Interpreter",this)
    private val eventStack = Stack<ForLoops>()
    init{
        frame.isVisible = true
    }
    var intVariables = HashMap<String,Int>()
    var stringVariables = HashMap<String,String>()
    var boolVariables = HashMap<String, Boolean>()
    val KEYWORDS : Array<String> = arrayOf("for","if","str","int","boolean","say")

    fun process(text : String){
        when(text.split(" ")[0]){
            "while" ->{}
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
                if(eventStack.peek().startEnq) {
                    eventStack.peek().startEnq = false
                    println(eventStack.size)
                    if(eventStack.size > 1){
                        var f = eventStack.pop()
                        var n = eventStack.peek()
                        for(processes in f.queue)
                            for(i in f.start..f.end)
                                n.queue.add(processes)
                    }
                    else eventStack.pop().begin()
                }
            }

            else ->{reassignment(text)}
        }

    }


    private fun reassignment(text : String){
        if(text.split(" ")[1] != "=")
            throw Exception("Syntax error")
        if(stringVariables.containsKey(text.split(" ")[0]))
            stringVariables[text.split(" ")[0]] = getVariableValue("str $text").toString()
        else if(intVariables.containsKey(text.split(" ")[0]))
            intVariables[text.split(" ")[0]] = processOperations(text.split("=")[1].replaceFirst(" ",""))
        else if(boolVariables.containsKey(text.split(" ")[0]))
            boolVariables[text.split(" ")[0]] = (text.split(" ")[2] == "true")
        else
            throw Exception("Error variable ${text.split(" ")[0]} has not been declared")
    }


    private fun processOperations(text: String): Int {
        var operationStack = Stack<Int>()
        var rpn = convertToRPN(text).replaceFirst(" ","")
        println(rpn)
        for (words in rpn.split(" ")) {
            println("LOOPS")
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
                            throw Exception()
                        }
                    }
                }
            } catch (e: Throwable) {
                throw Exception("Syntax error")
            }
        }

        return operationStack.peek()
    }


    private fun forLoops(text : String){
        val start = text.split(" ")[1].toInt()
        val end = text.split(" ")[3].toInt()
        require(text.split(" ")[2] == "in") { "Syntax Error" }
        require(text.split(" ")[4] == "{"){"Syntax Error"}
        var f = ForLoops(start,end,this)
        f.startEnq = true
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
            throw Exception("Error variable ${getVariableName(text)} has not been declared")
    }

    private fun addVariable(text : String){
        when(text.split(" ")[0]){
            "int" ->{intVariables[getVariableName(text)] = (processOperations(text.split("=")[1].replaceFirst(" ","")) as Int)}
            "str" ->{stringVariables[getVariableName(text)] = getVariableValue(text).toString()}
            "boolean" ->{boolVariables[getVariableName(text)] = (getVariableValue(text) as Boolean)}
        }
    }

    private fun getVariableName(text : String) : String{
        require(!text.split(" ")[1].matches(Regex("[a-zA-Z ]*\\d+.*"))) { "Syntax Error" }
        return text.split(" ")[1]
    }

    private fun getVariableValue(text : String) : Any? {
        when(text.split(" ")[0]){
            "int" ->{
                try{return text.split(" ")[3].toInt()}
                catch(e : Exception){"Syntax Error"}
            }
            "str" -> {
                var textChange = text
                if (textChange.count { "\"".contains(it) } > 2) {
                    throw Exception("Syntax Error")
                }
                var start = textChange.indexOf("\"")
                textChange = textChange.replaceFirst("\"", "")
                var end = textChange.indexOf("\"")

                return textChange.substring(start, end)
            }
            "boolean"->{
                if(text.split(" ")[3] != "true" && text.split(" ")[3] != "false")
                    throw Exception("Syntax Error")
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

        return str.reversed()
    }
}

private fun createAndShowGUI(){
    var interpreter = Interpreter()
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}