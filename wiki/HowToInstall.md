![Query Connector](https://raw.githubusercontent.com/balthier82/queryconnector/master/wiki/images/QueryConnector.png)

#Introduction

How to Install the extension on LibreOffice or OpenOffice installation.

#Details

*On Any Platform:*

 * Go to _Tools -> Extension Manager_ <br>
 * Click on *Add...* <br>
 * Select *queryconnector.oxt* <br>
 * Accept the prompted license <br>
 * Restart OpenOffice/LibreOffice
 * Enjoy! :D

*Only on Ubuntu with LibreOffice installed via apt-get:*

* Make sure that the package *libreoffice-java-common* is installed, otherwise the extension will not be registered properly. 

###Instructions for use unixODBC:

 * There is a bug due to the fact that the libraries "libodbc.so" and "libodbcinst.so" are not loaded from the libJdbcOdbc.so library (that implements the JDBC-ODBC bridge). After some research I found that you have to set the LD_PRELOAD environment variable this way:
   ```bash
   export LD_PRELOAD=$LD_PRELOAD:/usr/lib/libodbc.so:/usr/lib/libodbcinst.so
   ```

   assuming that the two files are located in /usr/lib. Without this setting OpenOffice/LibreOffice will crash.

 * Another problem I found is that the library libJdbcOdbc.so not find libjvm.so library. To solve it we have to change the LD_LIBRARY_PATH environment variable in this way:

   ```bash
   export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/java/default/lib/i386/client
   ```

 * If there is an error called *Invalid Handle* using the ODBC driver libmyodbc.so for MySQL, the problem is not the extension, but the driver itself. Other drivers are working properly.
