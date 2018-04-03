# This repository is no longer maintained.

Issue reports and pull requests will not be attended.


OC - Operations Center 

Operations Center functionality that is common across all clusters.

1. Management of the entire system - e.g. deploy a patch to the entire system
2. Manage specific clusters - e.g. restart all nodes in the AS Cluster in a round-robin fashion
3. Monitoring the entire system
4. Monitoring specific clusters - e.g. view system statistics for a specific cluster
5. Deploy artifacts to specific clusters - the current Artifact Distribution Coordinator (ADS) functionality 
6. View system topology - system will include all clusters
7. Navigate to a cluster from the system topology view & manage that cluster (item #2)
8. From the cluster topology view, connect to the primary manager node of that cluster, and run cluster specific operations. e.g. from OC cluster view, navigate to the management node of ESB, and carry out ESB specific management & monitoring operations (this is the same ESB management console)

How the OC Discovers the system topology
Each manager node in each cluster will contain the URL/hostname of the OC, and after that manager boots up, it will register itself with the OC. Registration information will include a connection information of the manager. Worker nodes are not aware of the OC. When a change in the cluster takes place (a new member joins, or an existing member leaves), the manager will inform the OC about this topology change.

The OC interacts directly only with the manager, and the manager is responsible for relaying the commands from the OC to the worker nodes.  e.g. if the OC issues a command (to AS manager) saying, restart AS cluster in a round robin fashion, the AS manager will execute that command in turn by sending out specific cluster messages within the AS cluster or by sending direct messages to the worker nodes in the AS cluster. This is a very scalable solution because managers act as agents of the OC, and get the job done on behalf of the OC,  and the OC will be able to handle a large number of clusters consisting of many worker nodes.

==

WSO2 Operations Center
