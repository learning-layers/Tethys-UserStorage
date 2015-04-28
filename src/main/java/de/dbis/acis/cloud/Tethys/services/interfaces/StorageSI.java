package de.dbis.acis.cloud.Tethys.services.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StorageSI {
	
	void createFile(InputStream is, String username, String path) throws IOException;
	void overwriteFile(InputStream is, String username, String path) throws IOException;
	void createDir(String username, String path) throws IOException;
	void getContent(OutputStream fos, String username, String path) throws IOException;
	void delete(String username, String path) throws IOException;
	boolean checkPathExists(String username, String path);
	
}
