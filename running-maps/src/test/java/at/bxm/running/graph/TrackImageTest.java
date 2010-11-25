package at.bxm.running.graph;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.testng.annotations.Test;
import at.bxm.running.xml.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;

// TODO base class for all tests ("test-utility" project?)
@Test
public class TrackImageTest extends TestBase {

	private void write(TrackImage data, int width, int height, String imageType, String filename)
					throws IOException {
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage image = gfxConf.createCompatibleImage(width, height);
		data.draw(image, data.getLatitudeMax(), data.getLatitudeMin(), data.getLongitudeMax(),
						data.getLongitudeMin());
		File target = getTestfile(filename);
		ImageIO.write(image, imageType, target);
	}

	public void drawLogbook() throws Exception {
		BufferedReader in = null;
		try {
			in = read("sample.fitlog");
			FitnessWorkbook fitlog = new XmlDecoder().parseLogbook(in);
			TrackImage track = new TrackImage(fitlog.getAthleteLogs().get(0).getActivities().get(0)
							.getTrack());
			write(track, 500, 500, "png", "image.png");
		} finally {
			in.close();
		}
	}

	// public void drawLogbook2() throws Exception {
	// BufferedReader in = null;
	// try {
	// in = read("bigsample.fitlog");
	// FitnessWorkbook fitlog = new XmlDecoder().parseLogbook(in);
	// List<Activity> activities = fitlog.getAthleteLogs().get(0).getActivities();
	// TrackImage track = new TrackImage(activities.get(51).getTrack());
	// track.write(500, 500, "png", "target/test-output/voesendorf.png");
	// } finally {
	// in.close();
	// }
	// }

}
