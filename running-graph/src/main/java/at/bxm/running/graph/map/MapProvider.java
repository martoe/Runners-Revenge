package at.bxm.running.graph.map;

public interface MapProvider {

	// FIXME define resolution
	MapLayout<?> getLayout(double latNorth, double latSouth, double lonEast, double lonWest,
					double resolution);

}
