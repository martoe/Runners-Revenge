package at.bxm.running.graph.map.providers;

import java.net.URI;

@Deprecated
public class GoogleMapsSatellite extends GoogleMaps {

	@Override
	URI getUrl(int x, int y, int zoom) {
		int serverNo = (int)Math.floor(Math.random() * 4);
		return URI.create("http://khm" + serverNo + ".google.com/kh/v=45&hl=en&x=" + x + "&y=" + y
						+ "&z=" + zoom + "&s=Galileo");
	}

	@Override
	int getMinZoomLevel() {
		return 6;
	}

	@Override
	int getMaxZoomLevel() {
		return 18;
	}

}
