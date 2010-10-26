package at.bxm.running.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

public class XmlDecoder {

	private static final char[] XML_PREFIX = new char[] { 239, 187, 191 };

	public void parseLogbook(BufferedReader in) throws IOException,
			XmlDecodingException {
		char[] prefix = new char[3];
		in.read(prefix);
		if (!Arrays.equals(prefix, XML_PREFIX)) {
			throw new XmlDecodingException(0, 0, "Invalid prefix characters");
		}
		Builder parser = new Builder();
		try {
			Document doc = parser.build(in);
			String namespace = doc.getRootElement().getNamespaceURI();
			// for (int i = 0; i <
			// doc.getRootElement().getChildElements().size(); i++) {
			// System.out.println(doc.getRootElement().getChildElements()
			// .get(i).getNamespaceURI());
			// }
			Element activities = doc.getRootElement().getFirstChildElement(
					"Activities", namespace);
			Element firstTrack = activities
					.getFirstChildElement("Activity", namespace)
					.getFirstChildElement("GPSRoute", namespace)
					.getFirstChildElement("TrackData", namespace);
			for (int i = 0; i < firstTrack.getChildCount(); i++) {
				System.out.println(firstTrack.getChild(i));
			}
		} catch (ParsingException e) {
			throw new XmlDecodingException(e.getLineNumber(),
					e.getColumnNumber(), e.getMessage(), e);
		}
	}

}
