package at.bxm.running.gui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import at.bxm.running.gui.ActiveWorkbook;
import at.bxm.running.gui.WorkbookListener;
import at.bxm.running.xml.FitnessWorkbook;

public class SimpleActivitiesView extends ViewPart {
	private ListViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new ListViewer(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
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

		// another way: use the object list directly
		// tableViewer.setContentProvider(new ArrayContentProvider());
		// tableViewer.setInput(ActiveWorkbook.getInstance().getData().getAthleteLogs().get(0).getActivities());

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
