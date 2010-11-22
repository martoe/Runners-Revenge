package at.bxm.running.graph.map;

@Deprecated
public interface MapProvider {

	MapLayout<?> getLayout(double latNorth, double latSouth, double lonEast, double lonWest,
					double resolution);

}
