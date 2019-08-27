import java.awt.BorderLayout
import java.awt.Color
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


class View(title : String,var interpreter : Interpreter) : JFrame() {
    private var area = JTextArea(1, 30)
    private var textField = JTextArea(17,30)
    private var border = BorderFactory.createLineBorder(Color.BLACK)

    init {
        area.border = BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10))
        textField.border = BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10))
        createUI(title)
    }

    public fun updateText(text : String){
        textField.text += ">>$text\n"
        interpreter.process(text)
    }

    public fun say(text : String){
        textField.text += text+"\n"
    }

    private fun createUI(title: String) {
        setTitle(title)
        defaultCloseOperation = (JFrame.EXIT_ON_CLOSE)
        setSize(400, 370)
        isResizable = false;
        setLocationRelativeTo(null)

        val layout = BorderLayout()
        contentPane.layout = layout
        textField.isEditable = false
        contentPane.add(textField,BorderLayout.PAGE_START)
        contentPane.add(area, BorderLayout.PAGE_END)
        area.addKeyListener(MyListener(area,this))
    }
}


