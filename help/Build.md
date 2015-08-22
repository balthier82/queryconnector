#How to Build

The queryconnector project is composed by two parts:

* LODBConnector
* QueryConnector

###LODBConnector

It's the GUI part of the extension. It has been developed with NetBeans and you
can easely modify and compile it using this IDE. Simply open the project with 
NetBeans.

###QueryConnector

It's the real extension for OpenOffice/LibreOffice that use LODBConnector to show the wizard.

#####Pre-build configuration:

[LibreOffice SDK Installation and Configuration](http://api.libreoffice.org/docs/install.html)
[OpenOffice SDK](http://www.openoffice.org/download/sdk/)

#####Build instruction:

* Open the prompt command windows/bash shell to the **QueryConnector** directory
* (Only for windows) Execute the **setsdkenv_windows.bat** that is located inside the sdk directory
* run **make**
* (you can clean the compiled file with **make clean**)
* The output file is located in **out/bin/**

