package at.bxm.running.xml;

import static org.testng.Assert.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.testng.annotations.Test;

@Test
public class JaxbTest {

	public void parseSmallLogbook() throws Exception {
		FitnessWorkbook fitlog = parse("sample.fitlog");
		assertEquals(fitlog.getAthleteLogs().size(), 1);
		check(fitlog.getAthleteLogs().get(0).getAthlete());
		assertEquals(fitlog.getAthleteLogs().get(0).getHistories().size(), 0);
		assertEquals(fitlog.getAthleteLogs().get(0).getActivities().size(), 1);
		check(fitlog.getAthleteLogs().get(0).getActivities().get(0).getTrack(), 126);
	}

	public void parseBigLogbook() throws Exception {
		FitnessWorkbook fitlog = parse("bigsample.fitlog");
		assertEquals(fitlog.getAthleteLogs().size(), 1);
		check(fitlog.getAthleteLogs().get(0).getAthlete());
		assertEquals(fitlog.getAthleteLogs().get(0).getHistories().size(), 0);
		assertEquals(fitlog.getAthleteLogs().get(0).getActivities().size(), 450); // FIXME 470???
	}

	private FitnessWorkbook parse(String filename) throws IOException, XmlDecodingException,
					InterruptedException {
		System.gc();
		Thread.sleep(1000);
		BufferedReader in = null;
		try {
			in = read(filename);
			return new XmlDecoder().parseLogbook(in);
		} finally {
			in.close();
		}
	}

	private void check(Athlete athlete) {
		assertNotNull(athlete);
		assertNotNull(athlete.getId());
		assertNotNull(athlete.getName());
	}

	private void check(Track track, int points) {
		assertNotNull(track);
		assertEquals(track.getPoints().size(), 126);
		for (TrackPoint point : track.getPoints()) {
			assertNotNull(point.getTm());
			assertTrue(point.getLatitude() != null && point.getLongitude() != null
							|| point.getHr() != null);
		}
	}

	private BufferedReader read(String resource) {
		return new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource)));
	}

}
