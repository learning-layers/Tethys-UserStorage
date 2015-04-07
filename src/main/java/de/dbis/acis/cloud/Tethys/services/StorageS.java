package de.dbis.acis.cloud.Tethys.services;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;

public class StorageS implements StorageSI{

	private final static String storageRoot = "/data/";

	@Override
	public void createFile(InputStream is, String path) {
		Path filePath = new File(storageRoot+path).toPath();
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
			Files.copy(is, new File(storageRoot+path).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createDir(String path) {
		try {
			Files.createDirectories(new File(storageRoot+path).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getContent(OutputStream fos,String path) {
		final Path filePath = new File(storageRoot+path).toPath();
		if(Files.isDirectory(filePath)){
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
				writer.write("{ Files : \n  [\n");
				for (Path file : Files.newDirectoryStream(filePath)) {
					writer.write("    { fileName : /"+file.subpath(2, file.getNameCount()).toString()+" }\n");
				}
				writer.write("  ]\n}");
				writer.flush();
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
			Files.delete(new File(storageRoot+path).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean checkPathExists(String path) {
		return Files.exists(new File(storageRoot+path).toPath());
	}
	
}
