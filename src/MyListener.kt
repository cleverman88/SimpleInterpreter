import java.awt.Color
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class MyListener(var component : Component, var frame : JFrame) : KeyListener {
    private val attrs = SimpleAttributeSet()
    private val sdoc: StyledDocument = (component as JTextPane).styledDocument


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
                StyleConstants.setForeground(attrs, Color.BLACK)
                sdoc.setCharacterAttributes(0, 100000, attrs, false);
                e.consume()
                (frame as View).updateText((component as JTextPane).text)
                (component as JTextPane).text = ""
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
        var check = (component as JTextPane).text
        for(words in check.split(" ")){
            if((frame as View).interpreter.KEYWORDS.contains(words)){
                StyleConstants.setForeground(attrs, Color.blue)
                sdoc.setCharacterAttributes(check.indexOf(words), check.indexOf(words)+words.length, attrs, false);
            }
        }
        if(check.count { "\"".contains(it) } == 2){
            var start = check.indexOf("\"")
            check = check.replaceFirst("\"", "")
            var end = check.indexOf("\"")
            StyleConstants.setForeground(attrs, Color.GREEN)
            sdoc.setCharacterAttributes(start, end, attrs, false);
        }
    }
}