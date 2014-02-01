/**
 * 
 */
package optgen

import org.xml.sax.Attributes;
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory

/**
 * @author adrian
 *
 */
class ProcessHandler extends DefaultHandler {
	
	def prefixOutFile
	def processingThreads = []
	
	ProcessHandler(def outputFilePrefix)
	{
		super()
		prefixOutFile = outputFilePrefix
	}

	void startElement(String ns, String localName, String qName,
		Attributes attrs) {
		if (qName == 'file') {
			def threadNumber = attrs.getValue("thread")
			def filePath = attrs.getValue("path")
			//fire off a thread
			def addedThread = Thread.start{
				println "Starting thread $threadNumber"
				def threadHandler = new ThreadHandler(threadNumber,
					prefixOutFile, filePath)
				def threadIn =
					SAXParserFactory.newInstance().newSAXParser().XMLReader
				threadIn.setContentHandler(threadHandler)
				threadIn.parse(new InputSource(new FileInputStream(filePath)))
			}
			processingThreads << addedThread
		}
	}
		
	void endDocument()
	{
		println "End of document ... now waiting for threads to complete"
		processingThreads.eachWithIndex { th, indx ->
			println "Joining $indx thread..."
			th.join()
		}
	}
		
	
	
}
