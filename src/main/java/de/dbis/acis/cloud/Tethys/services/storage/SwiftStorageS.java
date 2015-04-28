package de.dbis.acis.cloud.Tethys.services.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.inject.Inject;

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.TempAuthP;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.swift.v1.SwiftP;


public class SwiftStorageS implements StorageSI{
	
	ResourceBundle openstackRB = ResourceBundle.getBundle("openstack");
	
	private String swiftUserName;
	private String key;
	private SwiftP swiftP;
	private TempAuthP tempAuthP;
	
	@Inject
	public SwiftStorageS(SwiftP swiftP, TempAuthP tempAuthP) {
		this.swiftUserName = openstackRB.getString("swiftUserName");
		this.key = openstackRB.getString("swiftKey");
		this.swiftP = swiftP;
		this.tempAuthP = tempAuthP;
		
		System.out.println("create SwiftStorage");
	}

	@Override
	public void createFile(InputStream is, String username, String path) {
		swiftP.createContainer(username, username, token());
		swiftP.createOrReplaceObject(swiftUserName, username, path, token(), is);
		//TODO SHOULDN'T REPLACE HERE
	}

	@Override
	public void overwriteFile(InputStream is, String username, String path) {
		swiftP.createOrReplaceObject(swiftUserName, username, path, token(), is);
	}

	@Override
	public void createDir(String username, String path) {
		// We don't need Dir in Swift!
	}

	@Override
	public void getContent(OutputStream fos, String username, String path) {
		swiftP.getObjectContentAndMetadata(username, username, path, token());
	}

	@Override
	public void delete(String username, String path) {
		swiftP.deleteObject(username, username, path, token());
	}

	@Override
	public boolean checkPathExists(String username, String path) {
		// TODO Auto-generated method stub
		return true;
	}

	private String token() {
		return tempAuthP.getToken(swiftUserName, key).getHeaderString("X-Auth-Token");
	}
}
