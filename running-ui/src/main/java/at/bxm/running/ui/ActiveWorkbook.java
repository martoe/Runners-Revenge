package at.bxm.running.ui;

import at.bxm.running.core.FitnessWorkbook;
import at.bxm.running.xml.XmlDecoder;
import at.bxm.running.xml.XmlDecodingException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.eclipse.core.commands.common.EventManager;

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
		FileReader in = null;
		try {
			in = new FileReader(file);
			data = new XmlDecoder().parseLogbook(in);
			fireHandlerChanged();
		} finally {
			try {
				in.close();
			} catch (Exception ignore) {}
		}
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
