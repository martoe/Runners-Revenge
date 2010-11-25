package at.bxm.running.gui.views;

import at.bxm.running.core.Activity;
import at.bxm.running.core.FitnessWorkbook;
import at.bxm.running.core.Track;
import at.bxm.running.ui.ActiveWorkbook;
import at.bxm.running.ui.WorkbookListener;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

public class ActivitiesView extends ViewPart {
	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Platform.getAdapterManager().registerAdapters(adapterFactory, Contact.class);
		// getSite().setSelectionProvider(treeViewer);
		tableViewer.setLabelProvider(new LabelProvider());

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

			@Override
			public void dispose() {}

			@Override
			public Object[] getElements(Object inputElement) {
				FitnessWorkbook data = ((ActiveWorkbook)inputElement).getData();
				return data == null ? new Object[0] : data.getAthleteLogs().get(0).getActivities()
								.toArray();
			}
		});
		tableViewer.setInput(ActiveWorkbook.getInstance());

		TableViewerColumn viewerNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		viewerNameColumn.getColumn().setText("Startpoint Longitude");
		viewerNameColumn.getColumn().setWidth(300);
		viewerNameColumn.getColumn().setResizable(true);
		viewerNameColumn.getColumn().setMoveable(true);
		viewerNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Track track = ((Activity)cell.getElement()).getTrack();
				final String text;
				if (track == null) {
					text = "No Track";
				} else if (track.getPoints().isEmpty()) {
					text = "No Points";
				} else {
					text = String.valueOf(track.getPoints().get(0).getLongitude());
				}
				cell.setText(text);
			}
		});
		// TODO tableViewer.setComparator(ViewerComparator comparator)

		ActiveWorkbook.getInstance().addHandlerListener(new WorkbookListener() {
			@Override
			public void workbookLoaded() {
				tableViewer.refresh();
			}
		});
	}

	@Override
	public void setFocus() {}

}
