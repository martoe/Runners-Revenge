package at.bxm.running.graph;

import at.bxm.running.core.Track;
import at.bxm.running.core.TrackPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.SortedSet;
import java.util.TreeSet;

@Deprecated
public class TrackImage {

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
		Graphics gr = image.getGraphics();
		gr.setColor(Color.BLUE);

		// multiply each longitude with this value to place it on the image:
		double scaleFactorX = image.getWidth() / (lonEast - lonWest);
		// multiply each latitude with this value to place it on the image:
		double scaleFactorY = image.getHeight() / (latNorth - latSouth);

		int lastPointX = -1;
		int lastPointY = -1;
		for (TrackPoint thisPoint : track) {
			int thisPointX = (int)Math.round((thisPoint.getLongitude() - lonWest) * scaleFactorX);
			int thisPointY = (int)Math.round((latNorth - thisPoint.getLatitude()) * scaleFactorY);
			if (lastPointX >= 0) {
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
