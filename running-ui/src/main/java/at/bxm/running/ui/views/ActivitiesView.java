package at.bxm.running.ui.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import at.bxm.running.core.Activity;
import at.bxm.running.core.FitnessWorkbook;
import at.bxm.running.ui.ActiveWorkbook;
import at.bxm.running.ui.WorkbookListener;

public class ActivitiesView extends ViewPart {
	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		getSite().setSelectionProvider(tableViewer);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Platform.getAdapterManager().registerAdapters(adapterFactory, Contact.class);
		// getSite().setSelectionProvider(treeViewer);
		tableViewer.setLabelProvider(new LabelProvider());
		// FIXME tableViewer.setComparator(new ViewerComparator());
		createTableColumn(tableViewer, "Date", 80, new ActivityCellLabelProvider() {
			private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

			@Override
			String getText(Activity activity) {
				return activity.getStartTime() == null ? null : df.format(activity.getStartTime());
			}
		});
		createTableColumn(tableViewer, "Time", 60, new ActivityCellLabelProvider() {
			private final DateFormat df = new SimpleDateFormat("HH:mm");

			@Override
			String getText(Activity activity) {
				return activity.getStartTime() == null ? null : df.format(activity.getStartTime());
			}
		});
		createTableColumn(tableViewer, "Location", 150, new ActivityCellLabelProvider() {
			@Override
			String getText(Activity activity) {
				return activity.getLocation();
			}
		});
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

		ActiveWorkbook.getInstance().addHandlerListener(new WorkbookListener() {
			@Override
			public void workbookLoaded() {
				tableViewer.refresh();
			}
		});
	}

	@Override
	public void setFocus() {}

	private void createTableColumn(TableViewer target, String title, int size,
					ActivityCellLabelProvider provider) {
		TableViewerColumn viewerNameColumn = new TableViewerColumn(target, SWT.NONE);
		viewerNameColumn.getColumn().setText(title);
		viewerNameColumn.getColumn().setWidth(size);
		viewerNameColumn.getColumn().setResizable(true);
		viewerNameColumn.getColumn().setMoveable(true);
		viewerNameColumn.setLabelProvider(provider);
	}

	private static abstract class ActivityCellLabelProvider extends CellLabelProvider {
		@Override
		public final void update(ViewerCell cell) {
			Activity activity = (Activity)cell.getElement();
			cell.setText(getText(activity));
		}

		abstract String getText(Activity activity);
	}
}
