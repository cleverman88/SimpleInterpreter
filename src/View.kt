import java.awt.BorderLayout
import java.awt.Color
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.JTextPane
import java.awt.AWTEventMulticaster.getListeners
import javax.swing.text.StyleConstants
import javax.swing.text.SimpleAttributeSet
import java.awt.AWTEventMulticaster.getListeners
import javax.swing.text.StyledDocument
import java.awt.AWTEventMulticaster.getListeners
import java.awt.Dimension


class View(title : String,var interpreter : Interpreter) : JFrame() {
    var tp = JTextPane()
    private var textField = JTextPane()
    private var border = BorderFactory.createLineBorder(Color.BLACK)
    var indent = ""
    var tabs = 0

    init {
        tp.border = BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10))
        textField.border = BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10))
        createUI(title)
    }

    public fun updateText(text : String){
        textField.text += ">> $indent$text\n"
        try{
            interpreter.process(text)
        }
        catch(e : SyntaxError){
            textField.text += e.errorMsg+"\n"
        }
        catch(e : VariableNotDeclared){
            textField.text += e.errorMsg+"\n"
        }
    }

    public fun say(text : String){
        textField.text += "$indent$text\n"
    }

    private fun createUI(title: String) {
        setTitle(title)
        defaultCloseOperation = (JFrame.EXIT_ON_CLOSE)
        setSize(400, 370)
        isResizable = false;
        setLocationRelativeTo(null)
        textField.isEditable = false

        val scroll = JScrollPane(textField)
        contentPane.add(scroll)

        contentPane.add(tp, BorderLayout.PAGE_END)
        tp.addKeyListener(MyListener(tp,this))
    }
}


