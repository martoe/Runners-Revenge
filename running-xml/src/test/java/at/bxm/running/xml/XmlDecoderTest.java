package at.bxm.running.xml;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.testng.annotations.Test;

@Test
public class XmlDecoderTest {

	public void parseLogbook() throws Exception {
		BufferedReader in = read("bigsample.logbook");
		new XmlDecoder().parseLogbook(in);
		in.close();
	}

	private BufferedReader read(String resource) {
		return new BufferedReader(new InputStreamReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(resource)));
	}

}
