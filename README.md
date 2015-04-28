#Overview of operations-center
WSO2 Operations Center (WSO2 OC) is a tool to manage and monitor WSO2 production systems.

In businesses, delivering high quality, uninterrupted service to the users is utmost important to increase retentions. It is essential to continuously monitor the system to detect any risk related to continuous performance of the system and take immediate actions to solve or control the situation. WSO2 OC is an effort to make the monitoring an automated process which constantly monitor health statistics from the nodes and clusters, and trigger warning messages and alerts if system stability is affected by any reason.

Production systems must be kept updated with the patches to avoid any unnecessary behaviors. Traditional way of deploying patches is, copying the patch file to the patch directory of the product followed by a restart of the server. Which required some effort from the DevOps to manually perform, if the system is large. Another tedious task for DevOps is to manually maintain an inventory of the patches and servers that were updated with it. Currently there is no easy way to track and maintain all these information. 

 WSO2 OC make all these management and monitoring tasks much easier than ever before by automating monitoring tasks and increasing the visibility of the system metadata and system status to the DevOps.

Please find the WSO2 Operations-Center Milestone 2  architecture
* ![](https://lh4.googleusercontent.com/FXv65lgxQX_QvdjRJzDanzXw3v2bG0yu4_FvCVnjTzFNrK-9mh7D7fj7FagxDpFjkycCN2_jpBoXuaQ=w1347-h564)

#New features
* Manage specific clusters - e.g. restart all nodes in the AS Cluster in a round-robin fashion
* Monitoring the entire system (using the oc-topology / os-stat-table UES gadgets)
* Monitoring specific clusters - e.g. view system statistics for a specific cluster
View artifacts, patches and tenants
* View system topology - system will include all clusters
* Navigate to a cluster from the system topology view & manage that cluster (item #2)
* Publish any artifact information to OC (OC registers a OSGI service)

# Setup instructions
Operations Center contains 4 modules
1. WSO2OperationsCenter (Jax-Rs web app)
2. WSO2OperationsCenterAgent (Carbon component)
3. WSO2OperationsCenterArtifactExtractors (Carbon component)
4. WSO2OperationsCenterGadgets (WSO2 UES gadgets)

* Build #1, #2, #3 modules
* Add #1 to a Application Server
* [#2] Is the one extract server information from server node, It has to deploy in to $WSO2_HOME/repository/components/dropins/
* [#2] Copy src/main/repository/operations-center.xml configuration file to $WSO2_HOME/repository/conf/
* [#3] Is optional, It has different modules to extract server specific artifacts, deploy it to $WSO2_HOME/repository/components/dropins/
* [#4] copy the oc folder to $WSO2_HOME/repository/deployment/server/jaggeryapps/
Start WSO2 UES in portOffset=0
* Start WSO2 Application Server in portOffset=1
* Add WSO2 UES gadgets by external URL
    *  https://localhost:9443/oc/gadgets/oc-topology/oc-topology.xml
    * https://localhost:9443/oc/gadgets/oc-stat-table/oc-stat-table.xml


