buffalo
=======

web application instrumenting JBoss 7 to provide resource metrics to JBison

1. Deploy the war file (copy into JBOSS_HOME/standalone/deployments)

2. Start JBoss

3. Navigate to http://127.0.0.1:8080/buffalo/rest/datasource/ExampleDS to gather metrics for the ExampleDS data source

4. Deploy another data source (via mysql-ds.xml) and navigate to http://127.0.0.1:8080/buffalo/rest/datasource/TestDS/mysql-ds.xml

5. Create a JSON monitor in JBison (https://jbison.com) to monitor the metrics exposed by buffalo

More details on my blog http://kijanowski.blogspot.com/2013/02/collecting-monitoring-and-charting.html

It is possible to collect one particular metric of all data sources, for example the number of available connections in the pool:
http://127.0.0.1:8080/buffalo/rest/datasources/pool/AvailableCount
Available parameters are:
* ActiveCount
* AvailableCount
* AverageBlockingTime
* AverageCreationTime
* CreatedCount
* DestroyedCount
* MaxCreationTime
* MaxUsedCount
* MaxWaitTime
* TimedOut
* TotalBlockingTime
* TotalCreationTime

Or the number of Prepared Statement Cache Access:
http://127.0.0.1:8080/buffalo/rest/datasources/jdbc/PreparedStatementCacheAccessCount
Available parameters are:
* PreparedStatementCacheAccessCount
* PreparedStatementCacheAddCount
* PreparedStatementCacheCurrentSize
* PreparedStatementCacheDeleteCount
* PreparedStatementCacheHitCount
* PreparedStatementCacheMissCount

The JVM memory stats are available at:
http://127.0.0.1:8080/buffalo/rest/jvm/memory/heap-memory-usage
http://127.0.0.1:8080/buffalo/rest/jvm/memory/non-heap-memory-usage

The JVM memory pool stats are available at:
http://127.0.0.1:8080/buffalo/rest/jvm/memory-pool/{name}/{type}

where {name} is:
* Par_Eden_Space
* CMS_Old_Gen
* Par_Survivor_Space
* CMS_Perm_Gen
* Code_Cache

where {type} is:
* collection-usage-threshold
* collection-usage-threshold-count
* usage
* peak-usage
* collection-usage

The Thread usage metrics are available at:
http://127.0.0.1:8080/buffalo/rest/jvm/threading

The http/https connector metrics are available at:
http://127.0.0.1:8080/buffalo/rest/connector/http
http://127.0.0.1:8080/buffalo/rest/connector/https

the jms metrics are available at:
http://127.0.0.1:8080/buffalo/rest/messaging/default/jms-queue/testQueue?include-runtime=true
http://127.0.0.1:8080/buffalo/rest/messaging/default/jms-topic/testTopic?include-runtime=true