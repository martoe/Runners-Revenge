package at.bxm.running.graph.map;

public interface MapProvider {

	// FIXME define resolution
	MapLayout<?> getLayout(double longitudeMin, double latitudeMin, double longituteMax,
					double latitiudeMax, double resolution);

}
