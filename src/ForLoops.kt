import java.util.*

class ForLoops(override var start : Int, override var end : Int, var interpreter : Interpreter) : Commands(){
    override fun begin(){
        println("$start $end")
        for(i in start..end){
            for(processes in queue){
                interpreter.process(processes)
            }
        }
    }
}