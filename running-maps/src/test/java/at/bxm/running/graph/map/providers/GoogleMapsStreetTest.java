package at.bxm.running.graph.map.providers;

import static org.testng.Assert.*;
import at.bxm.running.graph.TestBase;
import at.bxm.running.graph.TrackImage;
import at.bxm.running.graph.map.MapLayout;
import at.bxm.running.graph.map.MapProvider;
import at.bxm.running.xml.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;
import at.bxm.running.xml.XmlDecodingException;
import java.io.BufferedReader;
import java.io.IOException;
import org.testng.annotations.Test;

@Test
public class GoogleMapsStreetTest extends TestBase {

	public void createTrackImage() throws IOException, XmlDecodingException {
		BufferedReader in = null;
		try {
			in = read("sample.fitlog");
			FitnessWorkbook fitlog = new XmlDecoder().parseLogbook(in);
			TrackImage track = new TrackImage(fitlog.getAthleteLogs().get(0).getActivities().get(0)
							.getTrack());
			MapProvider mapProvider = new GoogleMapsStreet();
			MapLayout<?> layout = mapProvider.getLayout(track.getLatitudeMax(), track.getLatitudeMin(),
							track.getLongitudeMax(), track.getLongitudeMin(), 17);
			assertEquals(layout.getTileRows(), 2);
			assertEquals(layout.getTileColumns(), 3);
			assertTrue(layout.getLatNorth() >= track.getLatitudeMax());
			assertTrue(layout.getLatSouth() <= track.getLatitudeMin());
			assertTrue(layout.getLonEast() >= track.getLongitudeMax());
			assertTrue(layout.getLonWest() <= track.getLongitudeMin());
			for (int i = 0; i < layout.getTileRows(); i++) {
				for (int j = 0; j < layout.getTileColumns(); j++) {
					assertTrue(layout.getTile(i, j).getImage().length > 2000, "Size of image " + i + "/" + j
									+ " is only " + layout.getTile(i, j).getImage().length);
					assertTrue(layout.getTile(i, j).getImage().length < 10000, "Size of image " + i + "/" + j
									+ " is too large: " + layout.getTile(i, j).getImage().length);
				}
			}
		} finally {
			in.close();
		}
	}

}
