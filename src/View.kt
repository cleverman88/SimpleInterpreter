import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import javax.swing.JTextPane



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
        catch(e : ArrayIndexOutOfBoundsException){
            textField.text += "Syntax error"+"\n"
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


