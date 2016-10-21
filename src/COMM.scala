import java.awt.Dimension
import javax.swing.{JButton, JTextArea}
import gnu.io._
import javax.swing._
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.{BufferedReader, IOException, InputStreamReader, OutputStream}

import scala.language.postfixOps


/**
  * Created by xeno on 16/10/21.
  */
class COMM(commID: String) extends JPanel with SerialPortEventListener {

  val portID = CommPortIdentifier.getPortIdentifier(commID)
  val port = portID.open("serial", 2000).asInstanceOf[SerialPort]
  val bufReader = new BufferedReader(new InputStreamReader(port.getInputStream))


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
    port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
    port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE)
    port.addEventListener(this)
    port.notifyOnDataAvailable(true)
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
        println("sStr = " + sStr)
      }

      try{
//        val portID = CommPortIdentifier.getPortIdentifier(commID)
//        val port = portID.open("serial", 2000).asInstanceOf[SerialPort]
        val out: OutputStream = port.getOutputStream
        val output: String = sStr + "\r\n"
        out.write(output.getBytes())
      } catch {
        case ex: Exception => ex.printStackTrace()
          println("catch OutputStream ....")
      }
    }
  }

  def serialEvent(event: SerialPortEvent): Unit = {
    val strBuf: StringBuffer = new StringBuffer()
    var recDat: Int = 0
    if(event.getEventType == SerialPortEvent.DATA_AVAILABLE){
      while(true){
        try{
          recDat = bufReader.read()
          if(recDat == -1) println("break")
          if(recDat != "\r") {
            strBuf.append(recDat)
            println("at serialEvent : recDat =" + recDat)
          } else {
            println("break")
          }
        } catch {
          case ex: IOException => ex.printStackTrace()
            System.exit(1)
        }
      }
      txt1.append("<--" + strBuf.toString + "\r\n")
    }
  }
}
