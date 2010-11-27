package at.bxm.running.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "at.bxm.running.gui";
	private static Activator plugin;
	private final Log logger = LogFactory.getLog(getClass());
	static {
		Logger.getRootLogger(); // FIXME forces load of the log4j bundle, but for JCL it is too late...
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		loadProperties();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private void loadProperties() throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("running.properties");
		if (in == null) {
			logger.warn("running.properties not found");
		} else {
			try {
				Properties props = new Properties();
				props.load(in);
				System.getProperties().putAll(props);
			} finally {
				try {
					in.close();
				} catch (Exception ignore) {}
			}
		}
	}
}
