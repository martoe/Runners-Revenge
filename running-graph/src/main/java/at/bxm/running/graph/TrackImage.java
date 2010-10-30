package at.bxm.running.graph;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.bxm.running.xml.Track;
import at.bxm.running.xml.TrackPoint;

/**
 * Hello world!
 * 
 */
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

	public void write(int width, int height, String imageType, String filename) throws IOException {
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage image = gfxConf.createCompatibleImage(width, height);
		Graphics gr = image.getGraphics();
		int lastPointX = -1;
		int lastPointY = -1;
		// factor to normalize a position (i.e. the longitude and latitude is in iterval [0,1]):
		double factor = Math.max(longitudeMax - longitudeMin, latitudeMax - latitudeMin);
		for (TrackPoint thisPoint : track) {
			int thisPointX = (int)Math.round((thisPoint.getLongitude() - longitudeMin) / factor * width);
			int thisPointY = height
							- (int)Math.round((thisPoint.getLatitude() - latitudeMin) / factor * height);
			if (lastPointX >= 0) { // TODO optimize calculation
				gr.drawLine(lastPointX, lastPointY, thisPointX, thisPointY);
			}
			lastPointX = thisPointX;
			lastPointY = thisPointY;
		}
		File target = new File(filename);
		logger.debug("Writing " + imageType + " image to " + target.getAbsolutePath());
		ImageIO.write(image, imageType, target);
	}

}
