import java.util.*

class ForLoops(start : Int, end : Int, var interpreter : Interpreter) : Commands(start, end){
    override fun begin(){
        println("$start $end")
        for(i in start..end){
            for(processes in queue){
                interpreter.process(processes)
            }
        }
    }
}