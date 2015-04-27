package de.dbis.acis.cloud.Tethys.services;

import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.TempAuthP;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.swift.v1.SwiftP;

public class SwiftStorageS implements StorageSI{
	
	@Inject
	SwiftP swift;
	
	@Inject
	TempAuthP tempAuth;

	@Override
	public void createFile(InputStream is, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void overwriteFile(InputStream is, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDir(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getContent(OutputStream fos, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkPathExists(String path) {
		// TODO Auto-generated method stub
		return false;
	}

}
