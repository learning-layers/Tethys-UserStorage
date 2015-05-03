package de.dbis.acis.cloud.Tethys.services.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StorageSI {
	
	void createFile(InputStream is, String oidcUserName, String pathToFile) throws IOException;
	void overwriteFile(InputStream is, String oidcUserName, String pathToFile) throws IOException;
	void createDir(String oidcUserName, String pathToFile) throws IOException;
	void getContent(OutputStream fos, String oidcUserName, String pathToFile) throws IOException;
	void delete(String oidcUserName, String pathToFile) throws IOException;
	boolean checkPathExists(String oidcUserName, String pathToFile);
	
}
