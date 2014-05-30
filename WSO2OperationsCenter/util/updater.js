

function updateData () {
	var log = new Log();
    log.debug("Updating data...");

    var date = new Date();
    var currentTime = date.getTime();

    var nodeCache = application.get('nodeCache');
    if (nodeCache != null){
        for (var index = 0; index < nodeCache.length; index++) {
            var node = nodeCache[index];
            if (node != null) {
                if (currentTime - node.reportedTime > 5000){
                    var misses = parseInt((currentTime - node.reportedTime) / 5000);
                    if(misses < 10) {
                        if(node.status == "RESTARTING" || node.status == "SHUTTING_DOWN") {
                            // Keep the status unchanged.
                        }else{
                            node.status = "MISSING_HB:" + misses;
                        }
                    }else{
                        node.status = "NOT_REPORTING";
                    }
                }
            }
        }
    }
};

var watchdog = setInterval(function(){updateData()},1000);
