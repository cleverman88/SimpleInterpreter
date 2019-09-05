import java.util.*

/**
 * Abstract class which any command that is run will inherit from
 */
public abstract class Commands {

    var queue = ArrayDeque<String>()
    open var start = 1;
    open var end = 1;

    public abstract fun begin()

}