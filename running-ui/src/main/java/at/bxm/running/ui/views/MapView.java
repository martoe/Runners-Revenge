package at.bxm.running.ui.views;

import at.bxm.running.core.Activity;
import at.bxm.running.maps.MapLayout;
import at.bxm.running.maps.MapProvider;
import at.bxm.running.maps.MapTile;
import at.bxm.running.maps.TrackImage;
import at.bxm.running.maps.TrackImage.TrackCanvas;
import at.bxm.running.maps.providers.GoogleMapsSatellite;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
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

public class MapView extends ViewPart implements ISelectionListener {
	private final Logger logger = Logger.getLogger(getClass());
	private Canvas canvas;
	private final MapProvider mapProvider = new GoogleMapsSatellite();
	private Activity selectedActivity;
	private SwtTrackCanvas trackCanvas;

	@Override
	public void createPartControl(Composite parent) {
		canvas = new Canvas(parent, SWT.BORDER);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (selectedActivity != null) {
					if (trackCanvas == null) {
						logger.debug("Drawing activity " + selectedActivity.getId() + "...");
						TrackImage track = new TrackImage(selectedActivity.getTrack());
						trackCanvas = createImage(e.gc.getDevice(), track, 13);
					}
					e.gc.drawImage(trackCanvas.getImage(), 0, 0);
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
			if (selectedElement instanceof Activity && !selectedElement.equals(selectedActivity)) {
				selectedActivity = (Activity)selectedElement;
				trackCanvas = null;
				canvas.redraw();
			}
		}
	}

	// TODO copied from MapService
	private SwtTrackCanvas createImage(Device device, TrackImage data, double resolution) {
		MapLayout<?> layout = mapProvider.getLayout(data.getLatitudeMax(), data.getLatitudeMin(),
						data.getLongitudeMax(), data.getLongitudeMin(), resolution);
		int width = layout.getTileWidth() * layout.getTileColumns();
		int height = layout.getTileHeight() * layout.getTileRows();
		logger.debug("Creating " + width + "*" + height + " image for " + layout);
		Image image = new Image(device, width, height);
		GC gr = new GC(image);
		gr.setForeground(new Color(device, 0, 0, 255));
		int y = 0;
		for (int row = 0; row < layout.getTileRows(); row++) {
			int x = 0;
			for (int col = 0; col < layout.getTileColumns(); col++) {
				MapTile tile = layout.getTile(row, col);
				try {
					InputStream in = tile.getImageStream();
					Image img = new Image(device, in);
					if (logger.isTraceEnabled()) {
						logger.trace("Placing image " + row + "/" + col + " at " + x + "/" + y);
					}
					gr.drawImage(img, x, y);
				} catch (IOException e) {
					logger.warn("Could not load image for " + tile, e);
				}
				x += layout.getTileWidth();
			}
			y += layout.getTileHeight();
		}
		SwtTrackCanvas c = new SwtTrackCanvas(image, gr);
		data.draw(c, layout.getLatNorth(), layout.getLatSouth(), layout.getLonEast(),
						layout.getLonWest());
		return c;
	}

	public static class SwtTrackCanvas implements TrackCanvas {
		private final Image image;
		private final GC graphics;

		public SwtTrackCanvas(Image image, GC gc) {
			this.image = image;
			graphics = gc;
		}

		@Override
		public double getWidth() {
			return image.getBounds().width;
		}

		@Override
		public double getHeight() {
			return image.getBounds().height;
		}

		@Override
		public void drawLine(int x1, int y1, int x2, int y2) {
			graphics.drawLine(x1, y1, x2, y2);
		}

		public Image getImage() {
			return image;
		}
	}

}
