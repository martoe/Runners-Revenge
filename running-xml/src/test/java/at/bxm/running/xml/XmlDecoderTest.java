package at.bxm.running.xml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.testng.annotations.Test;

@Test
public class XmlDecoderTest {

	public void parseLogbook() throws Exception {
		BufferedReader in = read("bigsample.logbook");
		new XmlDecoder().parseLogbook(in);
		in.close();
	}

	private BufferedReader read(String resource) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource), "UTF-8"));
	}

}
