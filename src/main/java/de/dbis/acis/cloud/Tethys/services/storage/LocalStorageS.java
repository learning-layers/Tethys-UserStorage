package de.dbis.acis.cloud.Tethys.services.storage;

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

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;

public class LocalStorageS implements StorageSI {
	
	private ResourceBundle configRB = ResourceBundle.getBundle("config");
	private String storageRootPath;
	
	public LocalStorageS() {
		this.storageRootPath = configRB.getString("storageRootPath");
		System.out.println("create LocalStorage with RootPath: " + this.storageRootPath);
	}

	@Override
	public void createFile(InputStream is, String username, String path) throws IOException {
		Path filePath = new File(pathFinder(username, path)).toPath();
			if(Files.notExists(filePath.getParent())) {
				Files.createDirectories(filePath.getParent());
			}
			Files.copy(is, filePath);
	}

	@Override
	public void overwriteFile(InputStream is, String username, String path) throws IOException {
			Files.copy(is, new File(pathFinder(username, path)).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public void createDir(String username,String path) throws IOException {
			Files.createDirectories(new File(pathFinder(username, path)).toPath());
	}

	@Override
	public void getContent(OutputStream fos, String username, String path) throws IOException {
		final Path filePath = new File(pathFinder(username, path)).toPath();
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

	@Override
	public void delete( String username, String path) throws IOException {
			Files.delete(new File(pathFinder(username, path)).toPath());
	}

	@Override
	public boolean checkPathExists(String username, String path) {
		return Files.exists(new File(pathFinder(username, path)).toPath());
	}
	
	private String pathFinder(String username, String path) {
		return this.storageRootPath + "/" + username + "/" + path;
	}
	
}
