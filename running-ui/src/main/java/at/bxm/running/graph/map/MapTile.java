package at.bxm.running.graph.map;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
public interface MapTile {

	byte[] getImage() throws IOException;

	InputStream getImageStream() throws IOException;

}
