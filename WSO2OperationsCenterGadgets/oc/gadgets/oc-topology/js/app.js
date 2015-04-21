$(function() {
	var appData;				//server data from  "/api/stat/server"
	var preSereverNodeCount; 	//to re create the root strut and update tree topology
	// var prefs = new gadgets.Prefs();
	// var dataFile = prefs.getString("dataSource");
	// var interval = parseFloat(prefs.getString("interval"));
	//console.log("app.js loaded!")
	// var api = "https://localhost:9443/oc/api/stat/server";


	/*
	index.jag
	<%
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "https://10.100.4.133:9443/test/");
	xhr.setRequestHeader("user" , "madhuka");
	xhr.send(); 
	print(xhr.responseText);
	%>*/
	var api = "https://localhost:9443/oc/gadgets/oc-topology/api.jag";

function getProductNameShort(longName) {
	var shortName = "";
	var splited = longName.split(" ");
	for (var i = 0; i < splited.length; i++) {
		var s = splited[i];
		if(contains(s, "WSO2")){
			continue;
		}
	    shortName += s[0];
	};

	if(!shortName) {
		shortName = "No_Name";
	}
	return shortName;
}

function contains(s1, s2) {
    return (s1.indexOf(s2) > -1);
}

function getClusters(clusterMap) {
	var clusters = [];
	
	var clusterKeys = Object.keys(clusterMap);

	clusterKeys.map(function(cKey) {
		var c = clusterMap[cKey];
		var cluster = {
			name: "Cluster " + getProductNameShort(c.clusterName),
			id_: c.domain,
			children: getChildren(c.nodes, c)
		}
		clusters.push(cluster);
	});
	return clusters;
	
}

function getChildren(nodeMap, cluster) {
	var nodes = [];
	var nodeKeys = Object.keys(nodeMap);
	// console.log(cluster)

	nodeKeys.map(function(nKey) {
		var n = nodeMap[nKey];
		if(n.serverUpTime == null) {
			n.serverUpTime = "";
		}
		// console.log(n)
		var node = {
			id_: "n"+n.nodeId,
			domain: cluster.domain,
			name: getProductNameShort(cluster.clusterName) + " Node [" + n.subDomain + "]",
			status: n.status,
			subDomain: n.subDomain,
			uptime: n.serverUpTime.split(' ').join(''),
			children: getMoreInfo(n, cluster)
		}
		nodes.push(node);
	});

	return nodes;
}

function getMoreInfo(node, cluster) {
	var n = node;
		// console.log(n)
	var moreInfo = {
		name: getProductNameShort(cluster.clusterName) + " Info",
		id_: "n"+n.nodeId,
		loadAverage: n.systemLoadAverage,
		threadCount: n.threadCount,
		freeMemory: n.freeMemory,
		version: cluster.clusterVersion,
		patches: n.patches,
		url: n.adminServiceUrl,
		children: [getPatches(n), getTenants(cluster)]
		// children: [getPatches(n), getTenant(n)]
	}
	return [moreInfo];
}

function getPatches(n) {
	var data = {
		name: n.patches.length+" Patches",
		children: getChildPatches(n)
	}
	
	return data;
}

function getChildPatches(n) {
	var patches = [];
	for(var i = 0; i < n.patches.length; i++) {
		var data = {
			name: n.patches[i]
		}
		patches.push(data);
	}
	
	return patches;
}

function getTenants(c) {
	var data = {
		name: c.tenants.length+" Tenants",
		children: getChildTenants(c.tenants)
	}
	return data;
}

function getChildTenants(tenants) {
	var tenantsArray = [];
	tenants.map(function(t) {

		var data = {
			name: t.domain,
			id_: t.domain,
			domain: t.domain,
			active: t.active,
			email: t.email,
			createdDate: t.createdDate
		}

		tenantsArray.push(data);
	});
	
	return tenantsArray;
}

var data;
	function getAppData() {
		//console.log(dataFile);
		$.getJSON(api+"?cid=null&nid=null&command=null", function(result) {
			appData = result;
		});
		
		if(appData){
			data = getClusters(appData);
		}

		// console.log(data)
	}

	function viewData() {
		if(data) {
			var newRoot = {};
			newRoot.name = "root";
			newRoot.children = data;
			if(newRoot){
				init(newRoot);
				update(newRoot)
			}

		}
	}

	function updateData() {
		if(appData) {
			//NEW START
			var clusterKeys = Object.keys(appData);
			clusterKeys.map(function(cKey) {
				var cluster = appData[cKey];
				var nodeKeys = Object.keys(cluster.nodes);

				nodeKeys.map(function(nKey) {
					var n = cluster.nodes[nKey];

					var id = n.nodeId;
				
					var uptime = n.serverUpTime.split(' ').join('');
					var cpu = parseFloat(n.systemCpuUsage).toPrecision(3);
					var loadAverage = n.systemLoadAverage;
					var threadCount = n.threadCount;
					var freeMemory = parseFloat(n.freeMemory).toPrecision(5);
					var url = n.adminServiceUrl;
					// var version = n.static.version;

					var status = n.status;



					d3.select("#n"+id+"-uptime").text(uptime);						
					d3.select("#n"+id+"-cpu").text(cpu)

					//----
					// d3.select("#"+id+"-version").text(version);						
					d3.select("#n"+id+"-memory").text(freeMemory +"MB")
					d3.select("#n"+id+"-load").text(loadAverage);						
					d3.select("#n"+id+"-thread").text(threadCount)

					d3.select("#n"+id+"-uptime").style("font-size", "10px");
					d3.select("#n"+id+"-cpu").style("font-size", "10px");


					if(status != "RUNNING") {
						d3.select("#n"+id+"-mgt").style("fill", "#FF1300");
						d3.select("#n"+id+"-worker").style("fill", "#FF1300");
					}else{
						d3.select("#n"+id+"-mgt").style("fill", "url(#dpurpleG)");
						d3.select("#n"+id+"-worker").style("fill", "url(#dblueG)");
					}
				});
				//console.log(appData[0].id)
				if(appData.length > preSereverNodeCount) {
					viewData();						
				}

			});

		}
	}

	//app start timers
	setInterval(getAppData, 1000);
	setTimeout(viewData, 4000);
	setInterval(updateData, 1000);
	


	

	
});