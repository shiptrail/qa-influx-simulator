package clientupdates

import java.io.InputStream
import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor
import scala.xml.{Elem, NodeSeq}


trait XmlBasedParser {
  def whenDefined(parameter: NodeSeq): Option[String] = {
    val text: String = parameter.text
    if (text.isEmpty) {
      None
    } else {
      Some(text)
    }
  }

  def whenDefinedNoneEmptySeq(parameter: NodeSeq): Option[NodeSeq] = {
    if (parameter.isEmpty) {
      Some(Seq.empty)
    } else {
      Some(parameter)
    }
  }


  def getElementsWithNameFromInputStream(ElementName: String, is: InputStream): Iterator[Elem] = {
    val trkptSplitter = XmlStreamElementProcessor.collectElements(_.lastOption.contains(ElementName))
    import XmlStreamElementProcessor.IteratorCreator._
    trkptSplitter.processInputStream(is)
  }
}
