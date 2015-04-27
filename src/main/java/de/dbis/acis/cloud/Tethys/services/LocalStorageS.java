package de.dbis.acis.cloud.Tethys.services;

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

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;

public class LocalStorageS implements StorageSI {

	private String storageRootPath;
	
	public LocalStorageS(String storageRootPath) {
		System.out.println("create LocalStorage with RootPath: "+storageRootPath);
		this.storageRootPath = storageRootPath;
	}

	@Override
	public void createFile(InputStream is, String path) {
		Path filePath = new File(storageRootPath+path).toPath();
		try {
			if(Files.notExists(filePath.getParent())) {
				Files.createDirectories(filePath.getParent());
			}
			Files.copy(is, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void overwriteFile(InputStream is, String path) {
		try {
			Files.copy(is, new File(storageRootPath+path).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createDir(String path) {
		try {
			Files.createDirectories(new File(storageRootPath+path).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getContent(OutputStream fos,String path) {
		final Path filePath = new File(storageRootPath+path).toPath();
		if(Files.isDirectory(filePath)){
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				writer.write("{ Files : \n  [\n");
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(filePath);
				for (Path file : dirStream) {
					writer.write("    { fileName : /"+file.subpath(2, file.getNameCount()).toString()+" }\n");
				}
				writer.write("  ]\n}");
				writer.flush();
				dirStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else if(Files.isRegularFile(filePath)) {
			try {
				Files.copy(filePath, fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void delete(String path) {
		try {
			Files.delete(new File(storageRootPath+path).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean checkPathExists(String path) {
		return Files.exists(new File(storageRootPath+path).toPath());
	}
	
}
