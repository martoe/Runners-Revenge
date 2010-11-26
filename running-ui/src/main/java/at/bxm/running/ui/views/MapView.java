package at.bxm.running.ui.views;

import at.bxm.running.core.Activity;
import at.bxm.running.core.TrackPoint;
import at.bxm.running.maps.MapLayout;
import at.bxm.running.maps.MapProvider;
import at.bxm.running.maps.MapService;
import at.bxm.running.maps.MapTile;
import at.bxm.running.maps.TrackImage;
import at.bxm.running.maps.providers.GoogleMapsSatellite;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapView extends ViewPart implements ISelectionListener {
	private final Logger logger = LoggerFactory.getLogger(MapView.class);
	private Canvas canvas;
	private final MapProvider mapProvider = new GoogleMapsSatellite();
	private Activity selectedActivity;
	private Image activityImage;

	@Override
	public void createPartControl(Composite parent) {
		canvas = new Canvas(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (selectedActivity != null) {
					if (activityImage == null) {
						logger.debug("Drawing " + selectedActivity + "...");
						TrackImage track = new TrackImage(selectedActivity.getTrack());
						MapService mapService = new MapService();
						mapService.setMapProvider(mapProvider);
						try {
							activityImage = createImage(e.gc.getDevice(), track, 17);
						} catch (IOException ex) {
							logger.warn("Could not create image for " + track, e);
						}
					}
					e.gc.drawImage(activityImage, 0, 0);
				}
			}
		});
	}

	@Override
	public void setFocus() {}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
			if (selectedElement instanceof Activity) {
				selectedActivity = (Activity)selectedElement;
				activityImage = null;
				canvas.redraw();
			}
		}
	}

	// TODO copied from MapService
	private Image createImage(Device device, TrackImage data, double resolution) throws IOException {
		MapLayout<?> layout = mapProvider.getLayout(data.getLatitudeMax(), data.getLatitudeMin(),
						data.getLongitudeMax(), data.getLongitudeMin(), resolution);
		int width = layout.getTileWidth() * layout.getTileColumns();
		int height = layout.getTileHeight() * layout.getTileRows();
		logger.debug("Creating " + width + "*" + height + " image for " + layout);
		Image image = new Image(device, width, height);
		GC gr = new GC(image);

		int y = 0;
		for (int row = 0; row < layout.getTileRows(); row++) {
			int x = 0;
			int colHeight = 0;
			for (int col = 0; col < layout.getTileColumns(); col++) {
				MapTile tile = layout.getTile(row, col);
				InputStream in = tile.getImageStream();
				Image img = new Image(device, in);
				if (logger.isTraceEnabled()) {
					logger.trace("Placing image " + row + "/" + col + " at " + x + "/" + y);
				}
				gr.drawImage(img, x, y);
				x += img.getBounds().width;
				colHeight = img.getBounds().height;
			}
			y += colHeight;
		}
		draw(image, gr, data, layout.getLatNorth(), layout.getLatSouth(), layout.getLonEast(),
						layout.getLonWest());
		return image;
	}

	// FIXME copied from TrackImage
	public void draw(Image image, GC gr, TrackImage data, double latNorth, double latSouth,
					double lonEast, double lonWest) {
		gr.setForeground(new Color(image.getDevice(), 0, 0, 255));

		// multiply each longitude with this value to place it on the image:
		double scaleFactorX = image.getBounds().width / (lonEast - lonWest);
		// multiply each latitude with this value to place it on the image:
		double scaleFactorY = image.getBounds().height / (latNorth - latSouth);

		int lastPointX = -1;
		int lastPointY = -1;
		for (TrackPoint thisPoint : data.getTrack()) {
			int thisPointX = (int)Math.round((thisPoint.getLongitude() - lonWest) * scaleFactorX);
			int thisPointY = (int)Math.round((latNorth - thisPoint.getLatitude()) * scaleFactorY);
			if (lastPointX >= 0) {
				gr.drawLine(lastPointX, lastPointY, thisPointX, thisPointY);
			}
			lastPointX = thisPointX;
			lastPointY = thisPointY;
		}
	}

}
