package at.bxm.running.xml;

import at.bxm.running.core.FitnessWorkbook;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

public class XmlDecoder {

	/** UTF-8 chars to eat up before the XML content starts */
	private static final char[] XML_PREFIX = new char[] { 65279 };// { 239 , 187, 191 };
	private final Logger logger = Logger.getLogger(getClass());

	public void readPrefix(Reader in) throws IOException, XmlDecodingException {
		char[] prefix = new char[XML_PREFIX.length];
		in.read(prefix);
		if (!Arrays.equals(prefix, XML_PREFIX)) {
			throw new XmlDecodingException(0, 0, "Invalid prefix characters");
		}
	}

	/**
	 * Since the file has a (binary) prefix before the XML content starts, it is not recognized as
	 * UTF-8 file and must therefore be read as {@link InputStream}
	 */
	public FitnessWorkbook parseLogbook(File file) throws IOException, XmlDecodingException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			return new XmlDecoder().parseLogbook(in);
		} finally {
			in.close();
		}

	}

	public FitnessWorkbook parseLogbook(Reader in) throws IOException, XmlDecodingException {
		Runtime rt = Runtime.getRuntime();
		final long startParseMem = rt.totalMemory() - rt.freeMemory();
		final long startParse = System.currentTimeMillis();
		// BufferedReader schemaReader = null;
		try {
			readPrefix(in);
			JAXBContext jc = JAXBContext.newInstance(FitnessWorkbook.class);
			Unmarshaller u = jc.createUnmarshaller();
			// FIXME schema validation
			// SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			// schemaReader = new BufferedReader(new InputStreamReader(Thread.currentThread()
			// .getContextClassLoader().getResourceAsStream("fitnesslog2.xsd")));
			// Schema schema = sf.newSchema(new StreamSource(schemaReader));
			// u.setSchema(schema);
			return (FitnessWorkbook)u.unmarshal(in);
			// } catch (SAXException e) {
			// throw new XmlDecodingException(e);
		} catch (JAXBException e) {
			throw new XmlDecodingException(e);
		} finally {
			long endParse = System.currentTimeMillis();
			long endParseMem = rt.totalMemory() - rt.freeMemory();
			logger.info("Parsing took " + (endParse - startParse) + " ms, consumed "
							+ (endParseMem - startParseMem) + " bytes");
			try {
				in.close();
			} catch (Exception ignore) {}
			// try {
			// schemaReader.close();
			// } catch (Exception ignore) {}
		}
	}

}
