package at.bxm.running.graph.map;

public class GoogleMapProvider implements MapProvider {

	@Override
	public MapLayout<GoogleMapTile> getLayout(double longitudeMin, double latitudeMin,
					double longituteMax, double latitiudeMax, double resolution) {
		// TODO implement
		DefaultMapLayout<GoogleMapTile> layout = new DefaultMapLayout<GoogleMapTile>();
		layout.addTile(0, 0, new GoogleMapTile("filename"));
		return layout;
	}

	private static class GoogleMapTile extends AbstractMapTile {

		public GoogleMapTile(String uniqueFilename) {
			super(uniqueFilename);
		}

		@Override
		public byte[] loadImage() {
			// TODO implement
			return null;
		}

	}

}
