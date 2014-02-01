/**
 * 
 */
package optgen

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.InputSource

/**
 * @author adrian
 *
 */

class ThreadHandler extends DefaultHandler {
	
	def pages = [:]
	def instructionCount
	def fileName
	def PAGEOFFSET = 12
	def writer
	def filePath
	
	ThreadHandler(def threadN, def prefix, def path)
	{
		super()
		instructionCount = 0
		fileName = "${prefix}OPT${threadN}.bin"
		writer = new FileWriter(fileName)
		filePath = path
	}
	
	def gotPage(long pageNumber)
	{
		if (!pages[pageNumber])
			return false
		return true
	}
	
	void writeOutPageRecord(long page)
	{
		def pageHandler = new PageHandler(page, writer, PAGEOFFSET,
			instructionCount)
		def threadIn =
			SAXParserFactory.newInstance().newSAXParser().XMLReader
		threadIn.setContentHandler(pageHandler)
		threadIn.parse(new InputSource(new FileInputStream(filePath)))
	}
	
	void startElement(String ns, String localName, String qName,
		Attributes attrs) {
		
		switch (qName) {		
			case 'instruction':
			case 'load':
			case 'store':
			case 'modify':
			instructionCount++
			long address = Long.parseLong(attrs.getValue('address'), 16)
			long page = address >> PAGEOFFSET
			if (!gotPage(page)) {
					writeOutPageRecord(page)
					pages[page] = true
			}
			break;
		}
	}
		
	void endDocument()
	{
		writer.close()
		println "FINISHED THREAD"
	}
}
