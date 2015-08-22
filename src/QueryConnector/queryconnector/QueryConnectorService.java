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
import com.meserico.XQueryConnectorService;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.frame.XModel;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class QueryConnectorService {

	public static class QueryConnectorServiceImpl extends WeakBase implements XServiceInfo, XQueryConnectorService {
	
		static final String __serviceName= "com.meserico.QueryConnectorService";

		private XComponentContext compContext;
		
		public QueryConnectorServiceImpl(XComponentContext compContext){
			this.compContext = compContext;
		}
		
		// XQueryConnectorService
		public void updateAll(Object object) {
			XModel model = UnoRuntime.queryInterface(XModel.class, object);
			QueryConnector.updateAllThroughModel(compContext, model);
		}
		
		public void silentUpdateAll(Object object){
			XModel model = UnoRuntime.queryInterface(XModel.class, object);
			QueryConnector.silentUpdateAllThroughModel(compContext, model);
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
	}
	
	
	public static XSingleComponentFactory __getComponentFactory(String sImplName) {
        XSingleComponentFactory xFactory = null;

        if ( sImplName.equals( QueryConnectorServiceImpl.class.getName() ) )
            xFactory = Factory.createComponentFactory(QueryConnectorServiceImpl.class,
                                                      QueryConnectorServiceImpl.getServiceNames());

        return xFactory;
    }
}