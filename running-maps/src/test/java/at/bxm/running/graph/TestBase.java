package at.bxm.running.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

public abstract class TestBase {

	protected final Logger logger = Logger.getLogger(getClass());

	protected final BufferedReader read(String resource) {
		return new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(resource)));
	}

	protected final File getTestfile(String filename) {
		File file = new File("target/test-output/" + filename);
		logger.debug("Using testfile " + file.getAbsolutePath());
		return file;
	}
}
