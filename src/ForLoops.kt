import java.util.*

class ForLoops(var start : Int, var end : Int, var interpreter : Interpreter){
    var queue = ArrayDeque<String>()
    var startEnq : Boolean = false
    fun begin(){
        println("$start $end")
        for(i in start..end){
            for(processes in queue){
                interpreter.process(processes)
            }
        }
    }
}