/**
 * 
 */
package optgen

import javax.xml.parsers.SAXParserFactory
import org.xml.sax.*

/**
 * @author adrian
 *
 */
class ProcessControl {
	
	ProcessControl(def inputFile, def outputFilePrefix)
	{
		def processMapHandler = new ProcessHandler(outputFilePrefix)
		def procIn = SAXParserFactory.newInstance().newSAXParser().XMLReader
		procIn.setContentHandler(processMapHandler)
		procIn.parse(new InputSource(new FileInputStream(inputFile)))
	}

}

def optGenCli = new CliBuilder
	(usage:
		'ProcessControlFile -i [Control File] -o [Output File Name Prefix]')
optGenCli.i(longOpt: 'input', args:1, argName: 'control', optionalArg: false,
	'Input (control) file name')
optGenCli.o(longOpt: 'output', args:1, argName: 'output', optionalArg: false,
 'Prefix for output file names')

def optArgs = optGenCli.parse(args)

new ProcessControl(optArgs.i, optArgs.o)

