package de.dbis.acis.cloud.TethysUserStorage.services.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface class for accessing the backend storage.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public interface StorageSI {
	
	/**
	 * Creates a file.
	 * 
	 * @param is
	 * @param oidcUserName
	 * @param pathToFile
	 * @throws IOException
	 */
	void createFile(InputStream is, String oidcUserName, String pathToFile) throws IOException;
	
	/**
	 * Overwrites a file.
	 * 
	 * @param is
	 * @param oidcUserName
	 * @param pathToFile
	 * @throws IOException
	 */
	void overwriteFile(InputStream is, String oidcUserName, String pathToFile) throws IOException;
	
	/**
	 * Creates a directory. Maybe we don't need this method for every storage backend.
	 * 
	 * @param oidcUserName
	 * @param pathToFile
	 * @throws IOException
	 */
	void createDir(String oidcUserName, String pathToFile) throws IOException;
	
	/**
	 * Gets the content of a file or a directory
	 * 
	 * @param fos
	 * @param oidcUserName
	 * @param pathToFile
	 * @throws IOException
	 */
	void getContent(OutputStream fos, String oidcUserName, String pathToFile) throws IOException;
	
	/**
	 * Deletes a file or a directory.
	 * 
	 * @param oidcUserName
	 * @param pathToFile
	 * @throws IOException
	 */
	void delete(String oidcUserName, String pathToFile) throws IOException;
	
	/**
	 * Checks whether or not a file under a given path exists
	 * 
	 * @param oidcUserName
	 * @param pathToFile
	 * @return
	 */
	boolean checkPathExists(String oidcUserName, String pathToFile);
	
}
