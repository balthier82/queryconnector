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

* [LibreOffice SDK Installation and Configuration](http://api.libreoffice.org/docs/install.html)
* [OpenOffice SDK](http://www.openoffice.org/download/sdk/)

#####Build instruction:

* Open the prompt command windows/bash shell to the **QueryConnector** directory
* (Only for windows) Execute the **setsdkenv_windows.bat** that is located inside the sdk directory
* run **make**
* (you can clean the compiled file with **make clean**)
* The output file is located in **out/bin/**

#####for Windows users:

* download **UnixUtils** zip from (http://sourceforge.net/projects/unxutils/), extract the content to any directory and put  this directory to the **PATH** enviroment variable.
* download **mingw32-make** archive from (http://sourceforge.net/projects/mingw/files/MinGW/Extension/make/) (example **mingw32-make-3.81-xxxxxxxx.tar.gz**), extract it to any directory and put this directory to the **PATH** enviroment variable.
* Rename the **mingw32-make.exe** in **make.exe**
