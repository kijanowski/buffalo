buffalo
=======

web application instrumenting JBoss 7 to provide resource metrics to JBison

1. Deploy the war file (copy into JBOSS_HOME/standalone/deployments)

2. Start JBoss

3. Navigate to http://127.0.0.1:8080/buffalo/rest/datasource/ExampleDS to gather metrics for the ExampleDS data source

4. Deploy another data source (via mysql-ds.xml) and navigate to http://127.0.0.1:8080/buffalo/rest/datasource/TestDS/mysql-ds.xml

5. Create a JSON monitor in JBison (https://jbison.com) to monitor the metrics exposed by buffalo

More details on my blog http://kijanowski.blogspot.com/2013/02/collecting-monitoring-and-charting.html