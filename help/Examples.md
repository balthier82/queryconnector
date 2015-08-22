###Scripting Example

This script can be used inside a Basic macro:

```vbnet
qc = CreateUnoService("com.meserico.QueryConnectorService")
qc.updateAll(ThisComponent)
```

###External Language Example (Java)

we suppose that **text.ods** is a spreadsheet that contains at least one query attached:

```java
import ...

public class UpdateAll {
	public static void main(String[] argv){
		try {
            XComponentContext xContext = Bootstrap.bootstrap();

            XMultiComponentFactory xServiceManager = xContext.getServiceManager();

			Object desktop = xServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
            XComponentLoader xComponentLoader = cast(XComponentLoader.class, desktop );
			
			
            XComponent xDocument = xComponentLoader.loadComponentFromURL(toUrl("test.ods"), "_blank", 0, toProperties(
				"Hidden", true,
				"UpdateDocMode", "1"
			));
			
			Object queryConnector = xServiceManager.createInstanceWithContext("com.meserico.QueryConnectorService", xContext );
			XQueryConnectorService xQueryConnector = cast(XQueryConnectorService.class, queryConnector);
			xQueryConnector.silentUpdateAll(xDocument);
			
			XStorable storable = cast(XStorable.class, xDocument);
			storable.storeToURL(toUrl("test.xls"), toProperties(
				"Overwrite", true,
				"FilterName", "MS Excel 97"
			));
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        System.exit( 0 );
	}
	
	public static String toUrl(final String fName) throws Exception {
		StringBuffer sLoadUrl = new StringBuffer("file:///");
		sLoadUrl.append(new java.io.File(fName).getCanonicalPath().replace('\\', '/').replace("#", "%23"));
		return sLoadUrl.toString();
	}
	
	public static PropertyValue[] toProperties(Object... props){
		if(props.length % 2 != 0)
			throw new IllegalArgumentException("The arguments count must be even.");
		
		PropertyValue[] properties = new PropertyValue[props.length/2];
		for(int i=0, j=0; i<props.length; i+=2, j++){
			properties[j] = new PropertyValue();
			properties[j].Name = props[i].toString();
			properties[j].Value = props[i+1];
		}
		return properties;
	}
	
	public static <T> T cast(Class<T> klass, Object instance){
		return UnoRuntime.queryInterface(klass, instance);
	} 
}
```
an example on how compile and run the java example:

```bat
set JAVA_PATH32="<java sdk dir>\bin"
set JAVA=%JAVA_PATH32%\java
set JAVAC=%JAVA_PATH32%\javac

set OFFICE_PATH=<path to libreoffice/openoffice installation>
set UNO_CLASSES_PATH=%OFFICE_PATH%\program\classes
set CLASS_PATH=%UNO_CLASSES_PATH%\java_uno.jar;%UNO_CLASSES_PATH%\unoloader.jar;%UNO_CLASSES_PATH%\unoil.jar;%UNO_CLASSES_PATH%\juh.jar;<path to the QueryConnectorService.uno.jar>

%JAVAC% -cp %CLASS_PATH% -source 1.6 UpdateAll.java
%JAVA% -cp %CLASS_PATH%;.\ UpdateAll
```
you can find the **QueryConnectorService.uno.jar** inside the **queryconnetor*.oxt** (use a zip program to show the content).
