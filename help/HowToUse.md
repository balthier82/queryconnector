#Introduction

A brief explanation on how to use the extension

#Details

 1) Select the starting cell of the spreadsheet where the result of the query will be inserted.

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step1.png)

 2) Click on *Data > Query > Attach data...*

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step2.png)

 3) In the window that appears, click Start <br><br>
 4) Select the method by which you want to connect to the database: *JDBC* or *ODBC*

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step3.png)

 5a) Choosing the direct connection via JDBC, you have to choose the driver and specify the connection parameters

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step4.png)

> You have to ensure that the JDBC driver that you intend to use is installed and configured correctly. The .jar file of the driver have to be inserted on the LibreOffice/OpenOffice classpath. Go to **Tools->Options->Advanced** then click on the
**Class Path** button. In the shown dialog, click on the **Add Archive** button and select the .jar file that contain the driver.

 5b) If you decide, instead, to use ODBC, click on the *Select ODBC source ...* button and you will see the following dialog box...

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step5.png)

...that allows you to select the ODBC source from those configured at the user level, system level or through the use of *.dsn files.

 6) At the next step, you have to enter the query:

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step6.png)

 7) ...after, click Next and you will see a preview of the result:

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step7.png)

 8) Finally, check the checkbox in the *Settings* area if you want to store the connection information inside the document (**THE INFORMATION IS STORED WITHOUT ENCRYPTION, FOR NOW**)

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step8.png)

 9) Click *Finish* and you'll see the result of the query loaded on the spreadsheet

![](https://raw.githubusercontent.com/balthier82/queryconnector/master/help/images/howtouse_step9.png)


Enjoy! :D
