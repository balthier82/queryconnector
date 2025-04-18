# Release notes

**Version 1.4.2**
* Modify query fix

**Version 1.4.1**
* Added icons
* Added functionality to edit a previously linked query

**Version 1.4**
* Major extension revision.

**Version 1.3.1**
* Resolved the IllegalArgumentException error that occurs when using the extension menu action.

**Version 1.3.0**
* Introduced a **UNO Component Service** to enable scripting using Basic, JavaScript, and other languages within LibreOffice, as well as external languages like C++ or Java..
* See [Examples](https://github.com/balthier82/queryconnector/blob/master/help/Examples.md).

**Version 1.2.1**
* Enhanced support for executing multiple queries across different sheets using the 'Update All' menu item.

**Version 1.2.0**
 * Introduced functionality to support multiple queries across different sheets via the 'Update All' menu item
 * Fixed an issue related to handling null values

**Version 1.1.1**
 * New "Update All" menu item

**Version 1.1.0**
 - The window layouts and Swing components have been redesigned to address issues encountered with various Java Look-and-Feel implementations across operating systems.  
 - The main window is no longer configured as "Always on Top".
 - Resolved a bug in the ODBC Source Selection dialog where the folder selection dialog (used for choosing DSN file directories) was consistently displayed beneath the main dialog window.  
 - Added compatibility for `unixODBC` on Unix systems (limited to the JRE provided by Oracle).  
  *Note: Please refer to the Wiki page titled "How to Install" for detailed instructions.*

**Version 1.0.3**
 * Java classes have been recompiled using code level 1.6, replacing the previous level 1.7.

**Version 1.0.2**
 * Resolved issues arising from Unix's case-sensitive file system.

**Version 1.0.1**
 - Corrected various translation issues to enhance clarity and accuracy.  
 - The extension previously always stored connection information; this behavior is now optional.  
 - Options for the extension are now stored in metadata instead of user-defined attributes.

**Version 1.0**
 * First release
