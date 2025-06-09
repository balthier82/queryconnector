package com.meserico.queryconnector;
 
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.frame.XModel;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import javax.swing.JOptionPane;
import com.sun.star.task.XJobExecutor;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.registry.XRegistryKey;


public class QueryConnectorServiceImpl extends WeakBase implements XServiceInfo, XJobExecutor {

	static final String __serviceName= "com.meserico.QueryConnectorService";
	static final String __implementationName = QueryConnectorServiceImpl.class.getName();
	
	static final String UPDATE_ALL_EVENT = "updateAll";
	static final String ATTACH_EVENT = "attach";
	static final String INFO_EVENT = "info";
	static final String UPDATE_EVENT = "update";
	static final String MODIFY_EVENT = "modify";
	
	private static final String[] __serviceNames = {
		__serviceName
	};
	
	private XComponentContext compContext;
	
	public QueryConnectorServiceImpl(XComponentContext compContext){
		this.compContext = compContext;
	}
	
	public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( __implementationName ) )
            xFactory = Factory.createComponentFactory(QueryConnectorServiceImpl.class, __serviceNames);
        return xFactory;
    }
	
	public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(__implementationName,
        		__serviceNames,
                xRegistryKey);
    }
	
	//get the current component
	private XComponent getXComponent() {
		try {
		  XMultiComponentFactory xMCF = this.compContext.getServiceManager();
		  Object desktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", this.compContext);
		  XDesktop xDesktop = (XDesktop)UnoRuntime.queryInterface(XDesktop.class, desktop);
		  return xDesktop.getCurrentComponent();
		}
		catch (Exception t) {
		  JOptionPane.showMessageDialog(null, t.getMessage());
		}
 		return null;
	}
	
	

	//XServiceInfo
	public String getImplementationName(  ) {
		return __implementationName;
	}
	// XServiceInfo
	public boolean supportsService(String sServiceName) {
        return sServiceName.equals( __serviceName );
    }
	//XServiceInfo
	public String[] getSupportedServiceNames(  ) {
		return __serviceNames;
	}
 
	public void trigger(String sEvent) {
		if (Thread.currentThread().getContextClassLoader() == null) {
		  Thread.currentThread().setContextClassLoader(QueryConnectorServiceImpl.class.getClassLoader());
		}
		try
		{
			if (INFO_EVENT.equals(sEvent)){
				info(this.getXComponent());
			} else if (ATTACH_EVENT.equals(sEvent)){
				attach(this.getXComponent());
			} else if(UPDATE_ALL_EVENT.equals(sEvent)) {
				updateAll(this.getXComponent());
			} else if(UPDATE_EVENT.equals(sEvent)) {
				update(this.getXComponent());
			} else if(MODIFY_EVENT.equals(sEvent)){
				modify(this.getXComponent());
			}
		}
		catch(Throwable e) {
		  e.printStackTrace();
		}
	}
	
	// XQueryConnectorService
	private void attach(Object object) {
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.attach(compContext, model);
	}
	
	private void modify(Object object) {
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.modify(compContext, model);
	}
	
	private void update(Object object) {
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.update(compContext, model);
	}
	
	private void info(Object object) {
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.info(compContext, model);
	}
	
	public void updateAll(Object object) {
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.updateAll(compContext, model);
	}
	
	public void silentUpdateAll(Object object){
		XModel model = UnoRuntime.queryInterface(XModel.class, object);
		QueryConnector.silentUpdateAll(compContext, model);
	}
}