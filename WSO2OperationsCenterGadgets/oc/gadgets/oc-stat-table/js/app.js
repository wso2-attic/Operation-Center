var api_url = "https://localhost:9443/oc/gadgets/oc-stat-table/api.jag";
var ocData;
$(function() {

	setInterval(callAPI, 1000);
	setInterval(drawTable, 1000);


	function callAPI() {
		$.get(api_url, function(data) {
			ocData = data;			
		});
	}

	var oTable = $('table').dataTable({
	    'fnDrawCallback': function(){
	        // $('td').addClass('hello')
	    }
	  });


	function drawTable() {

		if(!ocData)
			return;

		oTable.fnClearTable();

		var clusterMap = JSON.parse(ocData); 
		var clusterKeys = Object.keys(clusterMap);

		var domain, ip, subDomain, adminServiceUrl, 
			serverUpTime, totalMemory, freeMemory, threadCount, 
			status, patches;

		clusterKeys.map(function(cKey) {
			domain = cKey;

			var nodeMap = clusterMap[cKey];
			var nodeKeys = Object.keys(nodeMap);
			var nodes = nodeMap['nodes'];

			Object.keys(nodes).map(function(nodeId) {
				var node = nodes[nodeId];
				ip = node.ip;
				subDomain = node.subDomain;
				adminServiceUrl = node.adminServiceUrl;
				serverUpTime = node.serverUpTime;
				totalMemory = node.totalMemory;
				freeMemory = node.freeMemory;
				threadCount =node.threadCount;
				status = node.status;
				patches = node.patches;

				oTable.fnAddData([domain, ip, subDomain, adminServiceUrl, 
				serverUpTime, totalMemory, freeMemory, threadCount, status, patches]);
				// oTable.fnDraw();
			});
		})
      
	}
})