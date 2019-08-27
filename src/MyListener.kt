import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JTextArea

class MyListener(var component : Component,var frame : JFrame) : KeyListener {
    /**
     * Invoked when a key has been released.
     * See the class description for [KeyEvent] for a definition of
     * a key released event.
     * @param e the event to be processed
     */
    override fun keyReleased(e: KeyEvent?) {
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for [KeyEvent] for a definition of
     * a key pressed event.
     * @param e the event to be processed
     */
    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            if(e.keyCode == KeyEvent.VK_ENTER){
                e.consume()
                (frame as View).updateText((component as JTextArea).text)
                (component as JTextArea).text = ""
            }
        }
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for [KeyEvent] for a definition of
     * a key typed event.
     * @param e the event to be processed
     */
    override fun keyTyped(e: KeyEvent?) {
    }
}