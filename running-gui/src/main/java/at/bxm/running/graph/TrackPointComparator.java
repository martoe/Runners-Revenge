package at.bxm.running.graph;

import at.bxm.running.xml.TrackPoint;
import java.util.Comparator;

@Deprecated
public class TrackPointComparator implements Comparator<TrackPoint> {

	public static final TrackPointComparator INSTANCE = new TrackPointComparator();

	@Override
	public int compare(TrackPoint o1, TrackPoint o2) {
		double val1 = o1 == null ? 0 : o1.getTm();
		double val2 = o2 == null ? 0 : o2.getTm();
		return (int)Math.signum(val1 - val2);
	}

}
