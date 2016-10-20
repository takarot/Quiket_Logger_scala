import java.awt.Dimension
import javax.swing.{JButton, JTextArea}
import gnu.io.SerialPort
import gnu.io.CommPortIdentifier
import gnu.io.PortInUseException
import gnu.io.NoSuchPortException
import javax.swing._
import java.awt._
import java.awt.event.{ActionListener, _}
import java.io.OutputStream
import scala.language.postfixOps


/**
  * Created by xeno on 16/10/21.
  */
class COMM(commID: String) extends JPanel {

  val txt1: JTextArea = new JTextArea("")
  val txt2: JTextArea = new JTextArea("")
  txt1.setPreferredSize(new Dimension(200,200))
  txt2.setPreferredSize(new Dimension(200, 20))
  val btn_send = new JButton("送信")
  setLayout(new BorderLayout(5, 5))
  add(btn_send,BorderLayout.EAST)
  add(txt1,BorderLayout.CENTER)
  add(txt2,BorderLayout.SOUTH)

  try {
    val portID = CommPortIdentifier.getPortIdentifier(commID)
    val port = portID.open("serial", 2000).asInstanceOf[SerialPort]
    port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
    port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE)
  } catch {
    case ex: Exception => ex.printStackTrace()
      println("catch PortID ...")
  }

  btn_send.addActionListener(new ButtonListener(commID))


  class ButtonListener(commID: String) extends ActionListener {

    def actionPerformed(e: ActionEvent) {
      val sStr = txt2.getText

      if (sStr != "") {
        txt1.append("-->" + sStr + "\r\n")
        txt2.setText("")
      }

      try{
        val portID = CommPortIdentifier.getPortIdentifier(commID)
        val port = portID.open("serial", 2000).asInstanceOf[SerialPort]
        val out: OutputStream = port.getOutputStream
        val output: String = sStr + "\r\n"
        out.write(output.getBytes())
      } catch {
        case ex: Exception => ex.printStackTrace()
          println("catch OutputStream ....")
      }
    }
  }
}
