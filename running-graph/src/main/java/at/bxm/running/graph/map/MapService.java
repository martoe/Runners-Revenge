package at.bxm.running.graph.map;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.bxm.running.graph.TrackImage;

// TODO wiring stuff
public class MapService {

	private final Log logger = LogFactory.getLog(getClass());
	private MapProvider mapProvider;

	public void setMapProvider(MapProvider value) {
		mapProvider = value;
	}

	public BufferedImage createImage(TrackImage data, double resolution) throws IOException {
		MapLayout<?> layout = mapProvider.getLayout(data.getLatitudeMax(), data.getLatitudeMin(),
						data.getLongitudeMax(), data.getLongitudeMin(), resolution);
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
		data.draw(image, layout.getLatNorth(), layout.getLatSouth(), layout.getLonEast(),
						layout.getLonWest());
		return image;
	}

}
