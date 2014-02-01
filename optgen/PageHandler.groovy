/**
 * 
 */
package optgen

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler

/**
 * @author adrian
 *
 */
class PageHandler extends DefaultHandler {
	
	long page
	def writer
	def instructionCount
	def PAGEOFFSET
	def begin
	
	PageHandler(long pageIn, def writeOut, def offset, def startInstruction)
	{
		super()
		println("Now parsing for page $pageIn")
		page = pageIn
		writer = writeOut
		PAGEOFFSET = offset
		writer.write("PAGE: $page")
		instructionCount = 0
		begin = startInstruction
	}
	
	void startElement(String ns, String localName, String qName,
		Attributes attrs) {
		
		switch (qName) {
			case 'instruction':
			case 'load':
			case 'store':
			case 'modify':
			instructionCount++
			if (instructionCount >= begin) {
				long address = Long.parseLong(attrs.getValue('address'), 16)
				long pageFromAddress = address >> PAGEOFFSET
				if (page == pageFromAddress)
					writer.write(", $instructionCount")
			}
			break;
		}
	}
		
	void endDocument()
	{
		writer.write("\n")
	}

}
