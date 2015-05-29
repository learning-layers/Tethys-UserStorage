package de.dbis.acis.cloud.TethysUserStorage.services.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

import de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI;

/**
 * Implementation of the StorageServiceInterface to store data in locally.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class LocalStorageS implements StorageSI {
	
	/**
	 * Gets configuration from properties file.
	 */
	private ResourceBundle configRB = ResourceBundle.getBundle("config");
	private String storageRootPath;
	
	/**
	 * Constructor of the LocalStorageService
	 */
	public LocalStorageS() {
		this.storageRootPath = configRB.getString("storageRootPath");
		System.out.println("create LocalStorage with RootPath: " + this.storageRootPath);
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#createFile(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void createFile(InputStream is, String oidcUserName, String pathToFile) throws IOException {
		Path filePath = new File(pathFinder(oidcUserName, pathToFile)).toPath();
			if(Files.notExists(filePath.getParent())) {
				Files.createDirectories(filePath.getParent());
			}
			Files.copy(is, filePath);
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#overwriteFile(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void overwriteFile(InputStream is, String oidcUserName, String pathToFile) throws IOException {
			Files.copy(is, new File(pathFinder(oidcUserName, pathToFile)).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#createDir(java.lang.String, java.lang.String)
	 */
	@Override
	public void createDir(String oidcUserName,String pathToFile) throws IOException {
			Files.createDirectories(new File(pathFinder(oidcUserName, pathToFile)).toPath());
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#getContent(java.io.OutputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void getContent(OutputStream fos, String oidcUserName, String pathToFile) throws IOException {
		final Path filePath = new File(pathFinder(oidcUserName, pathToFile)).toPath();
		if(Files.isDirectory(filePath)){
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				writer.write("{ Files : \n  [\n");
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(filePath);
				for (Path file : dirStream) {
					writer.write("    { fileName : /"+file.subpath(2, file.getNameCount()).toString()+" }\n");
				}
				writer.write("  ]\n}");
				writer.flush();
				dirStream.close();
		} else if(Files.isRegularFile(filePath)) {
				Files.copy(filePath, fos);
		}
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete( String oidcUserName, String pathToFile) throws IOException {
			Files.delete(new File(pathFinder(oidcUserName, pathToFile)).toPath());
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI#checkPathExists(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkPathExists(String oidcUserName, String pathToFile) {
		return Files.exists(new File(pathFinder(oidcUserName, pathToFile)).toPath());
	}
	
	/**
	 * @param oidcUserName
	 * @param pathToFile
	 * @return
	 */
	private String pathFinder(String oidcUserName, String pathToFile) {
		return this.storageRootPath + "/" + oidcUserName + "/" + pathToFile;
	}
	
}
