package at.bxm.running.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Deprecated
public class XmlDecoder {

	private static final char[] XML_PREFIX = new char[] { 239, 187, 191 };

	public void readPrefix(Reader in) throws IOException, XmlDecodingException {
		char[] prefix = new char[3];
		in.read(prefix);
		if (!Arrays.equals(prefix, XML_PREFIX)) {
			throw new XmlDecodingException(0, 0, "Invalid prefix characters");
		}
	}

	public FitnessWorkbook parseLogbook(Reader in) throws IOException, XmlDecodingException {
		BufferedReader schemaReader = null;
		try {
			readPrefix(in);
			JAXBContext jc = JAXBContext.newInstance(FitnessWorkbook.class);
			Unmarshaller u = jc.createUnmarshaller();
			return (FitnessWorkbook)u.unmarshal(in);
		} catch (JAXBException e) {
			throw new XmlDecodingException(e);
		} finally {
			try {
				in.close();
			} catch (Exception ignore) {}
			try {
				schemaReader.close();
			} catch (Exception ignore) {}
		}
	}

}
