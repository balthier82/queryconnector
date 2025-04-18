# Introduction

Steps to install the extension on LibreOffice.

# Details

*On Any Platform:*

- Go to **Tools -> Extension Manager**  
- Click **Add...**  
- Select **queryconnector*.oxt**  
- Accept the license agreement when prompted  
- Restart LibreOffice  
- Enjoy! 😄

*Only on Ubuntu (and possibly other Linux distributions) with LibreOffice installed via apt-get:*

- Ensure the **libreoffice-java-common** package is installed; otherwise, the extension will not register correctly.

### Instructions for unixODBC:

* There is a bug because the libraries "libodbc.so" and "libodbcinst.so" are not loaded by the "libJdbcOdbc.so" library (which implements the JDBC-ODBC bridge). After some research, I found that you need to set the `LD_PRELOAD` environment variable as follows:

   ```bash
   export LD_PRELOAD=$LD_PRELOAD:/usr/lib/libodbc.so:/usr/lib/libodbcinst.so
   ```

   assuming that both files are located in /usr/lib. Without this setting, LibreOffice will crash.

 * Another issue I encountered is that the libJdbcOdbc.so library cannot locate the libjvm.so library. To resolve this, you need to modify the LD_LIBRARY_PATH environment variable as follows:

   ```bash
   export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/java/default/lib/i386/client
   ```

 * If you encounter an error labeled *Invalid Handle* while using the ODBC driver libmyodbc.so for MySQL, the issue lies with the driver itself, not the extension. Other drivers function correctly.
