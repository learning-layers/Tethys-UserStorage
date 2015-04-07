package de.dbis.acis.cloud.Tethys.services.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

public interface StorageSI {
	
	void createFile(InputStream is, String path);
	void overwriteFile(InputStream is, String path);
	void createDir(String path);
	void getContent(OutputStream fos, String path);
	void delete(String path);
	boolean checkPathExists(String path);
}
