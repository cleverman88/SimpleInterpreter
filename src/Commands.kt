import java.util.*

public abstract class Commands (var start : Int = 1, var end : Int = 2){

    var queue = ArrayDeque<String>()


    public abstract fun begin()

}