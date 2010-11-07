package at.bxm.running.graph.map;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO wiring stuff
public class MapService {

	private final Log logger = LogFactory.getLog(getClass());
	private MapProvider mapProvider;

	public void setMapProvider(MapProvider value) {
		mapProvider = value;
	}

	// TODO right now this is only a test method
	public void loadImages(double longitudeMin, double latitudeMin, double longituteMax,
					double latitiudeMax, double resolution) throws IOException {
		MapLayout<?> layout = mapProvider.getLayout(longitudeMin, latitudeMin, longituteMax,
						latitiudeMax, resolution);
		for (int row = 0; row < layout.getTileRows(); row++) {
			for (int col = 0; col < layout.getTileColumns(); col++) {
				MapTile tile = layout.getTile(row, col);
				tile.getImage();
			}
		}
	}

}
