package at.bxm.running.graph.map;

import at.bxm.running.graph.TestBase;
import at.bxm.running.graph.TrackImage;
import at.bxm.running.xml.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;
import at.bxm.running.xml.XmlDecodingException;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.testng.annotations.Test;

@Test
public class MapServiceTest extends TestBase {

	public void createHomeImage() throws IOException {
		// download my home at various zoom levels
		MapProvider mp = new GoogleMapsProvider();
		for (int i = 6; i < 19; i++) {
			MapLayout<?> layout = mp.getLayout(48.147533416748, 48.147533416748, 16.3582229614258,
							16.3582229614258, i);

			GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getDefaultScreenDevice().getDefaultConfiguration();
			BufferedImage image = gfxConf.createCompatibleImage(
							layout.getTileWidth() * layout.getTileColumns(),
							layout.getTileHeight() * layout.getTileRows());
			Graphics gr = image.getGraphics();

			int x = 0;
			for (int row = 0; row < layout.getTileRows(); row++) {
				int y = 0;
				int height = 0;
				for (int col = 0; col < layout.getTileColumns(); col++) {
					MapTile tile = layout.getTile(row, col);
					InputStream in = tile.getImageStream();
					BufferedImage img = ImageIO.read(in);
					if (img != null) {
						if (logger.isTraceEnabled()) {
							logger.trace("Placing image " + row + "/" + col + " at " + x + "/" + y);
						}
						gr.drawImage(img, x, y, null);
						y += img.getWidth();
						height = img.getHeight();
					}
				}
				x += height;
			}
			File target = getTestfile("home" + i + ".png");
			ImageIO.write(image, "png", target);
		}
	}

	public void createTrackImage() throws IOException, XmlDecodingException {
		BufferedReader in = null;
		try {
			in = read("sample.fitlog");
			FitnessWorkbook fitlog = new XmlDecoder().parseLogbook(in);
			TrackImage track = new TrackImage(fitlog.getAthleteLogs().get(0).getActivities().get(0)
							.getTrack());
			MapProvider mp = new GoogleMapsProvider();
			MapService mapService = new MapService();
			mapService.setMapProvider(mp);
			for (int i = 6; i < 19; i++) {
				BufferedImage image = mapService.createImage(track, i);
				File target = getTestfile("track" + i + ".png");
				ImageIO.write(image, "png", target);
			}
		} finally {
			in.close();
		}
	}

}
