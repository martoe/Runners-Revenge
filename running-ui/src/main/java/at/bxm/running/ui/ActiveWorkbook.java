package at.bxm.running.ui;

import java.io.File;
import java.io.IOException;
import org.eclipse.core.commands.common.EventManager;
import at.bxm.running.core.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;
import at.bxm.running.xml.XmlDecodingException;

public class ActiveWorkbook extends EventManager {
	private static final ActiveWorkbook INSTANCE = new ActiveWorkbook();
	private File file;
	private FitnessWorkbook data;

	private ActiveWorkbook() {}

	public void open(File file) throws IOException, XmlDecodingException {
		if (data != null) {
			close();
		}
		this.file = file;
		data = new XmlDecoder().parseLogbook(file);
		fireHandlerChanged();
	}

	public static ActiveWorkbook getInstance() {
		return INSTANCE;
	}

	public void close() {
		data = null;
		file = null;
	}

	public FitnessWorkbook getData() {
		return data;
	}

	public void addHandlerListener(final WorkbookListener listener) {
		addListenerObject(listener);
	}

	public void removeHandlerListener(final WorkbookListener listener) {
		removeListenerObject(listener);
	}

	protected void fireHandlerChanged(/* final HandlerEvent handlerEvent */) {
		for (Object listener : getListeners()) {
			((WorkbookListener)listener).workbookLoaded();
		}
	}

}
