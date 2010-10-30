package at.bxm.running;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import at.bxm.running.graph.TrackImage;
import at.bxm.running.xml.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;

// TODO base class for all tests ("test-utility" project?)
@Test
public class TrackImageTest {

	private final Log logger = LogFactory.getLog(getClass());

	public void drawLogbook() throws Exception {
		BufferedReader in = null;
		try {
			in = read("sample.fitlog");
			FitnessWorkbook fitlog = new XmlDecoder().parseLogbook(in);
			TrackImage track = new TrackImage(fitlog.getAthleteLogs().get(0).getActivities().get(0)
							.getTrack());
			track.write(500, 500, "png", "image.png");
		} finally {
			in.close();
		}
	}

	private BufferedReader read(String resource) {
		return new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource)));
	}

}
