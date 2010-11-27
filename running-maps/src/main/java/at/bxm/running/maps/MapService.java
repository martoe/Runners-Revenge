package at.bxm.running.maps;

import at.bxm.running.maps.TrackImage.BufferedImageCanvas;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

// TODO wiring stuff
public class MapService {

	private final Logger logger = Logger.getLogger(getClass());
	private MapProvider mapProvider;

	public void setMapProvider(MapProvider value) {
		mapProvider = value;
	}

	public BufferedImage createImage(TrackImage data, double resolution) throws IOException {
		MapLayout<?> layout = mapProvider.getLayout(data.getLatitudeMax(), data.getLatitudeMin(),
						data.getLongitudeMax(), data.getLongitudeMin(), resolution);
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice().getDefaultConfiguration();
		int width = layout.getTileWidth() * layout.getTileColumns();
		int height = layout.getTileHeight() * layout.getTileRows();
		logger.debug("Creating " + width + "*" + height + " image for " + layout);
		BufferedImage image = gfxConf.createCompatibleImage(width, height);
		Graphics gr = image.getGraphics();

		int y = 0;
		for (int row = 0; row < layout.getTileRows(); row++) {
			int x = 0;
			int colHeight = 0;
			for (int col = 0; col < layout.getTileColumns(); col++) {
				MapTile tile = layout.getTile(row, col);
				InputStream in = tile.getImageStream();
				BufferedImage img = ImageIO.read(in);
				if (img != null) {
					if (logger.isTraceEnabled()) {
						logger.trace("Placing image " + row + "/" + col + " at " + x + "/" + y);
					}
					gr.drawImage(img, x, y, null);
					x += img.getWidth();
					colHeight = img.getHeight();
				}
			}
			y += colHeight;
		}
		data.draw(new BufferedImageCanvas(image), layout.getLatNorth(), layout.getLatSouth(),
						layout.getLonEast(), layout.getLonWest());
		return image;
	}

}
