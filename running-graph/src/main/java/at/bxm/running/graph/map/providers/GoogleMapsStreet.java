package at.bxm.running.graph.map.providers;

import java.net.URI;

public class GoogleMapsStreet extends GoogleMaps {

	@Override
	URI getUrl(int x, int y, int zoom) {
		int serverNo = (int)Math.floor(Math.random() * 4);
		return URI.create("http://mt" + serverNo + ".google.com/vt/lyrs=&hl=en&x=" + x + "&y=" + y
						+ "&z=" + zoom + "&s=Galileo");
	}

	@Override
	int getMinZoomLevel() {
		return 0;
	}

	@Override
	int getMaxZoomLevel() {
		return 20; // greater values are possible but don't make sense
	}

}
