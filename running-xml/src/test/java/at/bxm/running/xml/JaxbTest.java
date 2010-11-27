package at.bxm.running.xml;

import static org.testng.Assert.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import org.testng.annotations.Test;
import at.bxm.running.core.Activity;
import at.bxm.running.core.Athlete;
import at.bxm.running.core.FitnessWorkbook;
import at.bxm.running.core.Track;
import at.bxm.running.core.TrackPoint;

@Test
public class JaxbTest {

	public void parseSmallLogbook() throws Exception {
		FitnessWorkbook fitlog = parse("sample.fitlog");
		assertEquals(fitlog.getAthleteLogs().size(), 1);
		check(fitlog.getAthleteLogs().get(0).getAthlete());
		assertEquals(fitlog.getAthleteLogs().get(0).getHistories().size(), 0);
		assertEquals(fitlog.getAthleteLogs().get(0).getActivities().size(), 1);
		Activity activity = fitlog.getAthleteLogs().get(0).getActivities().get(0);
		assertEquals(activity.getLocation(), "Vösendorf");
		assertEquals(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(activity.getStartTime()),
						"29.08.2008 17:29");
		check(activity.getTrack(), 126);
	}

	public void parseBigLogbook() throws Exception {
		FitnessWorkbook fitlog = parse("bigsample.fitlog");
		assertEquals(fitlog.getAthleteLogs().size(), 1);
		check(fitlog.getAthleteLogs().get(0).getAthlete());
		assertEquals(fitlog.getAthleteLogs().get(0).getHistories().size(), 0);
		assertEquals(fitlog.getAthleteLogs().get(0).getActivities().size(), 450);
	}

	private FitnessWorkbook parse(String filename) throws IOException, XmlDecodingException,
					InterruptedException {
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
		assertEquals(track.getPoints().size(), points);
		for (TrackPoint point : track.getPoints()) {
			assertNotNull(point.getTm());
			assertTrue(point.getLatitude() != null && point.getLongitude() != null
							|| point.getHr() != null);
		}
	}

	private BufferedReader read(String resource) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource), "UTF-8"));
	}

}
