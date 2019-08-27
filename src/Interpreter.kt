import java.awt.EventQueue
import java.lang.Exception
import javax.swing.JFrame

class Interpreter(){
    private val frame : View = View("Simple Interpreter",this)

    init{
        frame.isVisible = true
    }
    var intVariables = HashMap<String,Int>()
    var stringVariables = HashMap<String,String>()
    var boolVariables = HashMap<String, Boolean>()

    fun process(text : String){
        when(text.split(" ")[0]){
            "while" ->{}
            "for" ->{}
            "int" ->{addVariable(text)}
            "str" ->{addVariable(text)}
            "boolean" ->{addVariable(text)}
            "say" ->{say(text)}
        }

    }

    private fun say(text : String){
        if(stringVariables.containsKey(getVariableName(text)))
            frame.say(stringVariables[getVariableName(text)].toString())
        else if(intVariables.containsKey(getVariableName(text)))
            frame.say(intVariables[getVariableName(text)].toString())
        else if(boolVariables.containsKey(getVariableName(text)))
            frame.say(boolVariables[getVariableName(text)].toString())
        else
            throw Exception("Error variable ${getVariableName(text)} has not been declared")
    }

    private fun addVariable(text : String){
        when(text.split(" ")[0]){
            "int" ->{intVariables[getVariableName(text)] = (getVariableValue(text) as Int)}
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
}

private fun createAndShowGUI(){
    var interpreter = Interpreter()
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}