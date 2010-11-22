package at.bxm.running.gui.commands;

import at.bxm.running.gui.ActiveWorkbook;
import at.bxm.running.xml.XmlDecodingException;
import java.io.File;
import java.io.IOException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenFileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setText("Select File");
		fileDialog.setFilterExtensions(new String[] { "*.fitlog" });
		fileDialog.setFilterNames(new String[] { "Fitness Log (*.fitlog)" });
		String selected = fileDialog.open();
		if (selected != null) {
			try {
				ActiveWorkbook.getInstance().open(new File(selected));
			} catch (IOException e) {
				throw new ExecutionException(e.getMessage(), e);
			} catch (XmlDecodingException e) {
				throw new ExecutionException(e.getMessage(), e);
			}
		}
		return null;
	}

}
