/*
	QueryConnector - Attach a query to a Calc document
    Copyright (C) 2013 Enrico Giuseppe Messina

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

 
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleComponentFactory;
import com.meserico.queryconnector.XQueryConnectorService;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.frame.XModel;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import javax.swing.JOptionPane;
import com.sun.star.task.XJobExecutor;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;

public class QueryConnectorService {

	public static class QueryConnectorServiceImpl extends WeakBase implements XServiceInfo, XJobExecutor, XQueryConnectorService {
	
		static final String __serviceName= "com.meserico.QueryConnectorService";

		static final String UPDATE_ALL_EVENT = "updateAll";
		static final String ATTACH_EVENT = "attach";
		static final String INFO_EVENT = "info";
		static final String UPDATE_EVENT = "update";
		static final String MODIFY_EVENT = "modify";
		
		private XComponentContext compContext;
		private com.sun.star.frame.XFrame m_xFrame;
		
		public QueryConnectorServiceImpl(XComponentContext compContext){
			this.compContext = compContext;
		}
		
		//get the current component
		private XComponent getXComponent()
		{
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

		//XServiceInfo
		public String getImplementationName(  ) {
			return QueryConnectorServiceImpl.class.getName();
		}
		// XServiceInfo
		public boolean supportsService(String sServiceName) {
            return sServiceName.equals( __serviceName );
        }
		//XServiceInfo
		public String[] getSupportedServiceNames(  ) {
			return getServiceNames();
		}
		
		private static String[] getServiceNames() {
            String[] sSupportedServiceNames = { __serviceName };
            return sSupportedServiceNames;
        }
 
		public void trigger(String sEvent) {
			if (Thread.currentThread().getContextClassLoader() == null) {
			  Thread.currentThread().setContextClassLoader(QueryConnectorService.class.getClassLoader());
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
	}
	
	
	public static XSingleComponentFactory __getComponentFactory(String sImplName) {
        XSingleComponentFactory xFactory = null;

        if ( sImplName.equals( QueryConnectorServiceImpl.class.getName() ) )
            xFactory = Factory.createComponentFactory(QueryConnectorServiceImpl.class,
                                                      QueryConnectorServiceImpl.getServiceNames());

        return xFactory;
    }
}