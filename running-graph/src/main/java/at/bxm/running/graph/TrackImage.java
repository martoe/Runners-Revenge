package at.bxm.running.graph;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.bxm.running.xml.Track;
import at.bxm.running.xml.TrackPoint;

public class TrackImage {

	private final Log logger = LogFactory.getLog(getClass());

	private final SortedSet<TrackPoint> track = new TreeSet<TrackPoint>(TrackPointComparator.INSTANCE);
	private final double latitudeMin;
	private final double latitudeMax;
	private final double longitudeMin;
	private final double longitudeMax;

	public TrackImage(Track data) {
		double latMin = Double.MAX_VALUE;
		double latMax = Double.MIN_VALUE;
		double lonMin = Double.MAX_VALUE;
		double lonMax = Double.MIN_VALUE;
		for (TrackPoint point : data.getPoints()) {
			if (point.getLatitude() != null && point.getLongitude() != null) {
				track.add(point);
				if (point.getLatitude() > latMax) {
					latMax = point.getLatitude();
				}
				if (point.getLatitude() < latMin) {
					latMin = point.getLatitude();
				}
				if (point.getLongitude() > lonMax) {
					lonMax = point.getLongitude();
				}
				if (point.getLongitude() < lonMin) {
					lonMin = point.getLongitude();
				}
			}
		}
		latitudeMin = latMin;
		latitudeMax = latMax;
		longitudeMin = lonMin;
		longitudeMax = lonMax;
	}

	/**
	 * draw the track onto an image
	 * @param image the image to draw this track
	 * @param latNorth the latitude of the image's north border
	 * @param latSouth the latitude of the image's south border
	 * @param lonEast the longitude of the image's east border
	 * @param lonWest the longitude of the image's west border
	 */
	public void draw(BufferedImage image, double latNorth, double latSouth, double lonEast,
					double lonWest) {
		int width = image.getWidth();
		int height = image.getHeight();
		Graphics gr = image.getGraphics();
		int lastPointX = -1;
		int lastPointY = -1;
		// factor to normalize a position (i.e. the longitude and latitude is in iterval [0,1]):
		double factor = Math.max(lonEast - lonWest, latNorth - latSouth);
		for (TrackPoint thisPoint : track) {
			int thisPointX = (int)Math.round((thisPoint.getLongitude() - lonWest) / factor * width);
			int thisPointY = height
							- (int)Math.round((thisPoint.getLatitude() - latSouth) / factor * height);
			if (lastPointX >= 0) { // TODO optimize calculation
				gr.drawLine(lastPointX, lastPointY, thisPointX, thisPointY);
			}
			lastPointX = thisPointX;
			lastPointY = thisPointY;
		}
	}

	public double getLatitudeMin() {
		return latitudeMin;
	}

	public double getLatitudeMax() {
		return latitudeMax;
	}

	public double getLongitudeMin() {
		return longitudeMin;
	}

	public double getLongitudeMax() {
		return longitudeMax;
	}

}
