package at.bxm.running.maps;

import at.bxm.running.core.Track;
import at.bxm.running.core.TrackPoint;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.SortedSet;
import java.util.TreeSet;

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
	 * 
	 * @param image the image to draw this track
	 * @param latNorth the latitude of the image's north border
	 * @param latSouth the latitude of the image's south border
	 * @param lonEast the longitude of the image's east border
	 * @param lonWest the longitude of the image's west border
	 */
	public void draw(TrackCanvas target, double latNorth, double latSouth, double lonEast,
					double lonWest) {
		// multiply each longitude with this value to place it on the image:
		double scaleFactorX = target.getWidth() / (lonEast - lonWest);
		// multiply each latitude with this value to place it on the image:
		double scaleFactorY = target.getHeight() / (latNorth - latSouth);

		int lastPointX = -1;
		int lastPointY = -1;
		for (TrackPoint thisPoint : track) {
			int thisPointX = (int)Math.round((thisPoint.getLongitude() - lonWest) * scaleFactorX);
			int thisPointY = (int)Math.round((latNorth - thisPoint.getLatitude()) * scaleFactorY);
			if (lastPointX >= 0) {
				target.drawLine(lastPointX, lastPointY, thisPointX, thisPointY);
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

	public SortedSet<TrackPoint> getTrack() {
		return track;
	}

	public static interface TrackCanvas {
		double getWidth();

		double getHeight();

		void drawLine(int x1, int y1, int x2, int y2);
	}

	public static class BufferedImageCanvas implements TrackCanvas {
		private final BufferedImage image;
		private final Graphics graphics;

		public BufferedImageCanvas(BufferedImage image) {
			this.image = image;
			graphics = image.getGraphics();
		}

		@Override
		public double getWidth() {
			return image.getWidth();
		}

		@Override
		public double getHeight() {
			return image.getHeight();
		}

		@Override
		public void drawLine(int x1, int y1, int x2, int y2) {
			graphics.drawLine(x1, y1, x2, y2);
		}
	}
}
