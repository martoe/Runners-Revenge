package at.bxm.running.xml;

import at.bxm.running.core.FitnessWorkbook;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class XmlDecoder {

	private static final char[] XML_PREFIX = new char[] { 239, 187, 191 };

	// FIXME private final Logger logger = LoggerFactory.getLogger(getClass());

	public void readPrefix(Reader in) throws IOException, XmlDecodingException {
		char[] prefix = new char[3];
		in.read(prefix);
		if (!Arrays.equals(prefix, XML_PREFIX)) {
			throw new XmlDecodingException(0, 0, "Invalid prefix characters");
		}
	}

	public FitnessWorkbook parseLogbook(Reader in) throws IOException, XmlDecodingException {
		Runtime rt = Runtime.getRuntime();
		final long startParseMem = rt.totalMemory() - rt.freeMemory();
		final long startParse = System.currentTimeMillis();
		BufferedReader schemaReader = null;
		try {
			readPrefix(in);
			JAXBContext jc = JAXBContext.newInstance(FitnessWorkbook.class);
			Unmarshaller u = jc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			schemaReader = new BufferedReader(new InputStreamReader(Thread.currentThread()
							.getContextClassLoader().getResourceAsStream("fitnesslog2.xsd")));
			Schema schema = sf.newSchema(new StreamSource(schemaReader));
			u.setSchema(schema);
			return (FitnessWorkbook)u.unmarshal(in);
		} catch (SAXException e) {
			throw new XmlDecodingException(e);
		} catch (JAXBException e) {
			throw new XmlDecodingException(e);
		} finally {
			long endParse = System.currentTimeMillis();
			long endParseMem = rt.totalMemory() - rt.freeMemory();
			// FIXME logger.info("Parsing took " + (endParse - startParse) + " ms, consumed "
			//				+ (endParseMem - startParseMem) + " bytes");
			try {
				in.close();
			} catch (Exception ignore) {}
			try {
				schemaReader.close();
			} catch (Exception ignore) {}
		}
	}

}
