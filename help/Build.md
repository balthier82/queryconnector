# How to Build

The QueryConnector project consists of two parts:

* LODBConnector
* QueryConnector

### LODBConnector

This is the GUI component of the extension. It was developed using **NetBeans**, and you can easily modify and compile it with this IDE. Simply open the project in NetBeans.

### QueryConnector

It is the core extension for LibreOffice that uses LODBConnector to display the wizard.

##### Pre-build configuration:

Before building it, you need to install the LibreOffice SDK. More information can be found at the following links:

* [LibreOffice SDK](http://api.libreoffice.org/docs/install.html)

##### Build instructions:

* Navigate to the **QueryConnector** directory using the command prompt or bash shell
* Run **setsdkenv_windows.bat** or **setsdkenv_unix.sh**, which are located in the LibreOffice SDK directory.
* run **make** (Note: The Makefile was designed exclusively for Windows. You must modify it before using it on a Unix-like system.)
* The output file can be found in the **out/bin/** directory.

##### Windows users only:

* download **UnixUtils** zip from (http://sourceforge.net/projects/unxutils/), extract the content to any directory and add that directory to the PATH environment variable.
* download **mingw32-make** archive from (http://sourceforge.net/projects/mingw/files/MinGW/Extension/make/) (example **mingw32-make-3.81-xxxxxxxx.tar.gz**), extract the content to any directory and add that directory to the PATH environment variable.
* Rename **mingw32-make.exe** to **make.exe**
