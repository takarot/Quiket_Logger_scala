import java.awt.BorderLayout
import javax.swing.JFrame
import scala.language.postfixOps

/**
  * Created by xeno on 16/10/21.
  */

object Quiket_Logger {
  def main(args: Array[String]): Unit ={
    val commID: String = args(0)
    new SComm(commID)
  }
}

class SComm(commID: String) {
    val frame: JFrame = new JFrame("""Serial Communication""")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    val h: COMM = new COMM(commID)
    frame.add(h, BorderLayout.CENTER)
    frame.pack()
    frame.setVisible(true)

}
