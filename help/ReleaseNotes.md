#Release notes

**Version 1.3.1**
* Suppressed the **IllegalArgumentException** exception that occurrs using the extension.

**Version 1.3.0**
* Added a **UNO Component Service** to allow scripting with Basic/Javascript/etc within OpenOffice/LibreOffice or with external language like C++ or Java.
* See [Examples](https://github.com/balthier82/queryconnector/blob/master/help/Examples.md).

**Version 1.2.1**
* Solved same problem with the "Update All" command

**Version 1.2.0**
 * Add support for multi query on different sheets with the "Update All" menu item
 * Solved a problem with the null value

**Version 1.1.1**
 * Add "Update All" menu item

**Version 1.1.0**
 * The layout of the windows and Swing components has been renovated to solve some problems found in different Look And Feel of Java used by operating systems.
 * The main window now is not set as Always on Top.
 * Fixed a bug with the ODBC Source Selection dialog in which the folder selection dialog (used to select the folder containing the DSN files) was always below that window.
 * Added support for unixODBC on Unix systems (works only on the JRE provided by Oracle) *NOTE: Please read the Wiki page "How to Install"*

**Version 1.0.3**
 * Java classes have been recompiled with code level 1.6 instead of 1.7

**Version 1.0.2**
 * Fixed some problems due to the fact that unix uses a case-sensitive file system.

**Version 1.0.1**
 * Fixed several shortcomings in the translation 
 * The extension always stored the connection information. Not any more, is optional. 
 * The extension's options are now saved on metadata rather than to the user-defined attributes.

**Version 1.0**
 * First release
