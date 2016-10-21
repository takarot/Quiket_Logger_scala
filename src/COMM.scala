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

  val txt1: JTextArea = new JTextArea("表示欄")
  txt1.setPreferredSize(new Dimension(300,400))
  val txt2: JTextArea = new JTextArea("入力欄")
  txt2.setPreferredSize(new Dimension(150, 20))
  val btn_send  = new JButton("se")
  val btn_save  = new JButton("save")
  val btn_del   = new JButton("del")
  val btn_clear = new JButton("clear")
  val btn_time  = new JButton("time")

  val btn_R = new JButton("R(R,←)")
  val btn_x = new JButton("x(X,↓)")
  val btn_o = new JButton("o(O,→)")

  val bl: BorderLayout = new BorderLayout(5,5)
  setLayout(bl)

  // setBounds(x, y, whidth, height)
  btn_save .setBounds(  0,350,75,25)
  btn_del  .setBounds( 75,350,75,25)
  btn_clear.setBounds(150,350,75,25)
  btn_time .setBounds(225,350,75,25)
  btn_send .setBounds(150,350,75,25)

  btn_R.setBounds(0, 375, 100, 25)
  btn_x.setBounds(100, 375, 100, 25)
  btn_o.setBounds(200, 375, 100, 25)

  add(btn_save)
  add(btn_del)
  add(btn_clear)
  add(btn_time)
  add(btn_send)

  add(btn_R)
  add(btn_x)
  add(btn_o)

  txt1.setBounds(0,0,200,375)
  txt2.setBounds(0,375,50,20)
  add(txt1, BorderLayout.CENTER)
  add(txt2, BorderLayout.SOUTH)

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
        println("send sStr = " + sStr)
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
          //noinspection ComparingUnrelatedTypes
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

  override def setBounds(i: Int, i1: Int, i2: Int, i3: Int): Unit = super.setBounds(i, i1, i2, i3)
  override def setPreferredSize(dimension: Dimension): Unit = super.setPreferredSize(dimension)
}
