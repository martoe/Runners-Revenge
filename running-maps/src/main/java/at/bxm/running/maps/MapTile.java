package at.bxm.running.maps;

import java.io.IOException;
import java.io.InputStream;

public interface MapTile {

	byte[] getImage() throws IOException;

	InputStream getImageStream() throws IOException;

}
