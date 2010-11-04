package at.bxm.running.graph.map;

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
		for (int z = 17; z >= 6; z--) {
			mapService.loadImages(x, y, x + 1, y + 1, z);
			x = x / 2;
			y = y / 2;
		}
	}

}
