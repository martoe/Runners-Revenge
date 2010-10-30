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
			if (point.getLat() != null && point.getLon() != null) {
				track.add(point);
				if (point.getLat() > latMax) {
					latMax = point.getLat();
				}
				if (point.getLat() < latMin) {
					latMin = point.getLat();
				}
				if (point.getLon() > lonMax) {
					lonMax = point.getLon();
				}
				if (point.getLon() < lonMin) {
					lonMin = point.getLon();
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
		TrackPoint lastPoint = null;
		for (TrackPoint thisPoint : track) {
			if (lastPoint != null) {
				double x1 = (thisPoint.getLon() - longitudeMin) / (longitudeMax - longitudeMin);
				double y1 = (thisPoint.getLat() - latitudeMin) / (latitudeMax - latitudeMin);
				double x2 = (lastPoint.getLon() - longitudeMin) / (longitudeMax - longitudeMin);
				double y2 = (lastPoint.getLat() - latitudeMin) / (latitudeMax - latitudeMin);
				gr.drawLine((int) Math.round(x1 * width), (int) Math.round(y1 * height),
								(int) Math.round(x2 * width), (int) Math.round(y2 * height));
			}
			lastPoint = thisPoint;
		}
		File target = new File(filename);
		logger.debug("Writing " + imageType + " image to " + target.getAbsolutePath());
		ImageIO.write(image, imageType, target);

	}

}
