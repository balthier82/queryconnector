# Introduction

A concise guide to using the extension

# Details

 1) Choose the initial cell of the spreadsheet where the query results will be inserted

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step1.png)

 2) Click on *Data > Connect query... > Attach data...*

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step2.png)

 3) In the dialog box that opens, select 'Start'. <br><br>
 4) Choose the connection method you wish to use for accessing the database: *JDBC* or *ODBC*

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step3.png)

 5a) If you opt for a direct connection via JDBC, you need to select the driver and provide the required connection parameters

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step4.png)

> Ensure that the JDBC driver you plan to use is properly installed and configured. The driver's .jar file must be added to the LibreOffice classpath. Navigate to **Tools -> Options -> Advanced**, and click on the **Class Path** button. In the dialog that appears, select **Add Archive** and choose the `.jar` file containing the driver.

 5b) If you prefer to use ODBC, click the *Select ODBC Source...* button to open the corresponding dialog box

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step5.png)

which lets you choose the ODBC source from those configured at the user level, system level, or via *.dsn files.

 6) In the following step, you will need to input the query

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step6.png)

 7) Next, click 'Next' to view a preview of the results:

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step7.png)

 8) Finally, check the box in the *Settings* area if you'd like to save the connection information within the document (**PLEASE NOTE: THE INFORMATION IS STORED IN PLAIN TEXT, WITHOUT ENCRYPTION**)

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step8.png)

 9) Click *Finish* to load the query results into the spreadsheet

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step9.png)


Enjoy! :D
