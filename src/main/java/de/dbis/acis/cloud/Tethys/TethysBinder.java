package de.dbis.acis.cloud.Tethys;


import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.dbis.acis.cloud.Tethys.services.StorageS;
import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;

/**
 * HK2 Binding class. Which binds classes for Dependency Injection(DI).
 * See "HK2", "JSR-330", "DI" and "CDI" for more Information.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class TethysBinder extends AbstractBinder {

	@Override
	protected void configure() {
		//Binding via Factory. In this Case too much overhead.
		//bindFactory(new ProxyFactory<ProxyKeystoneApi>(ProxyKeystoneApi.class)).to(ProxyKeystoneApi.class);
		//Binding by Hand. Faster than binding by Factory.
		bind(new StorageS()).to(StorageSI.class);
	}

}