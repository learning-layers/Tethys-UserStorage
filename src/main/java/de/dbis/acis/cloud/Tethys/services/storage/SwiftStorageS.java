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
	
	private String swiftTenantName;
	private String swiftUserName;
	private String swiftKey;
	private SwiftP swiftP;
	private TempAuthP tempAuthP;
	
	/**
	 * @param swiftP
	 * @param tempAuthP
	 */
	@Inject
	public SwiftStorageS(SwiftP swiftP, TempAuthP tempAuthP) {
		this.swiftTenantName = openstackRB.getString("swiftTenantName");
		this.swiftUserName = openstackRB.getString("swiftUserName");
		this.swiftKey = openstackRB.getString("swiftKey");
		this.swiftP = swiftP;
		this.tempAuthP = tempAuthP;
		
		System.out.println("create SwiftStorage");
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#createFile(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void createFile(final InputStream is, String oidcUserName, String pathToFile) {
		swiftP.createOrReplaceObject(swiftTenantName, oidcUserName, pathToFile, token(), is);
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#overwriteFile(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void overwriteFile(InputStream is, String oidcUserName, String pathToFile) {
		swiftP.createOrReplaceObject(swiftTenantName, oidcUserName, pathToFile, token(), is);
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#createDir(java.lang.String, java.lang.String)
	 */
	@Override
	public void createDir(String oidcUserName, String pathToFile) {
		//Instead of creating dir we use this method to create an Container for the user
		//this is no problem since checkPathExists always return true. So this task won't be triggered normally.
		swiftP.createContainer(swiftTenantName, oidcUserName, token());
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#getContent(java.io.OutputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void getContent(OutputStream fos, String oidcUserName, String pathToFile) {
		swiftP.getObjectContentAndMetadata(swiftTenantName, oidcUserName, pathToFile, token());
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String oidcUserName, String pathToFile) {
		swiftP.deleteObject(swiftTenantName, oidcUserName, pathToFile, token());
	}

	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI#checkPathExists(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkPathExists(String oidcUserName, String pathToFile) {
		// We don't have dirs in swift. We just have containers. So we don't need to check if a file exists! We just have to check if a container exists
		return true;
	}

	/**
	 * @return
	 */
	private String token() {
		String token = tempAuthP.getToken(swiftTenantName + ":" + swiftUserName, swiftKey).getHeaderString("X-Auth-Token");
		System.out.println("Token: " +token);
		return token;
	}
}
