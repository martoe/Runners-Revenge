package at.bxm.running.graph.map;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

@Test
public class MapServiceTest {

	private final Log logger = LogFactory.getLog(getClass());

	public void downloadInzersdorf() throws Exception {
		MapProvider mp = new GoogleMapProvider();
		MapService mapService = new MapService();
		mapService.setMapProvider(mp);
		int x = 71492;
		int y = 45483;
		for (int z = 17; z >= 16; z--) {
			mapService.loadImages(x, y, x + 1, y + 1, z);
			x = x / 2;
			y = y / 2;
		}
	}

	public void createHomeImage() throws IOException {
		MapProvider mp = new GoogleMapProvider();
		for (int i = 6; i < 11; i++) {
			MapLayout<?> layout = mp.getLayout(16.3582229614258, 48.147533416748, 16.3582229614258,
							48.147533416748, i);

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
			File target = new File("home" + i + ".png");
			logger.debug("Writing image to " + target.getAbsolutePath());
			ImageIO.write(image, "png", target);
		}
	}

}
