package at.bxm.running.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XmlDecoder {

	private static final char[] XML_PREFIX = new char[] { 239, 187, 191 };
	private final Log logger = LogFactory.getLog(getClass());

	public void readPrefix(BufferedReader in) throws IOException, XmlDecodingException {
		char[] prefix = new char[3];
		in.read(prefix);
		if (!Arrays.equals(prefix, XML_PREFIX)) {
			throw new XmlDecodingException(0, 0, "Invalid prefix characters");
		}
	}

	public FitnessWorkbook parseLogbook(BufferedReader in) throws IOException, XmlDecodingException {
		Runtime rt = Runtime.getRuntime();
		final long startParseMem = rt.totalMemory() - rt.freeMemory();
		final long startParse = System.currentTimeMillis();
		try {
			readPrefix(in);
			JAXBContext jc = JAXBContext.newInstance(FitnessWorkbook.class);
			Unmarshaller u = jc.createUnmarshaller();
			return (FitnessWorkbook) u.unmarshal(in);
			// return JAXB.unmarshal(in, FitnessWorkbook.class);
		} catch (JAXBException e) {
			throw new XmlDecodingException(e);
		} finally {
			long endParse = System.currentTimeMillis();
			long endParseMem = rt.totalMemory() - rt.freeMemory();
			logger.info("Parsing took " + (endParse - startParse) + " ms, consumed "
							+ (endParseMem - startParseMem) + " bytes");
			in.close();
		}
	}

}
