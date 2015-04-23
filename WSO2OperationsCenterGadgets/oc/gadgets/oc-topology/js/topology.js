var root;
var isRootSetupDone = false;
var api = "https://localhost:9443/oc/gadgets/oc-topology/api.jag";
var cpuValues;

var margin = {
    top: 20,
    right: 120,
    bottom: 20,
    left: 120
},
width = 960 - margin.right - margin.left,
height = 800 - margin.top - margin.bottom;



var i = 0,
    duration = 750,
    rectW = 180, //60
    rectH = 70,  //30
    padding = 10,
    zoomMin = 0.5,
    zoomMax = 3,
    iconW = 30,
    iconH = 20;

var rootText = "root";
var clusterText = "Cluster";
var serverText = " Node";
var moreInfoText = " Info";
var patchesText = "Patches";
var patchText = "patch";
var tenantsText = "Tenants";
var artifactsText = "Artifacts";
var endText = ".";




var tree = d3.layout.tree().nodeSize([rectW + padding, rectH + padding]);
var diagonal = d3.svg.diagonal()
    .projection(function (d) {
    return [d.x + rectW / 2, d.y + rectH / 2];
});

var svg = d3.select("#body").append("svg").attr("width", 3000).attr("height", 1000)
    .call(zm = d3.behavior.zoom().scaleExtent([zoomMin, zoomMax]).on("zoom", redraw)).append("g")
    .attr("transform", "translate(" + 350 + "," + 10 + ")");

//necessary so that zoom knows where to zoom and unzoom from
zm.translate([350, 20]);



function collapse(d) {
    if (d.children) {
        d._children = d.children;
        d._children.forEach(collapse);
        d.children = null;
    }
}

d3.select("#body").style("height", "800px");
//gradients
addGradient("#4A4A4A 0%, #2B2B2B 100%", "lblueG");
addGradient("#87FC70 0%, #0BD318 100%", "lgreenG");
// addGradient("#accb5f 0%, #7aae12 50%, #a2de48 100%", "lgreenG");
addGradient("#67e5db 0%, #30dcc2 100%", "sblueG");
addGradient("#EF4DB6 0%, #C643FC 100%", "dpurpleG");
addGradient("#403939 0%, #0a0909 100%", "lblackG");
addGradient("#dedede 0%, #ffffff 39%, #ffffff 100%", "lashG");
addGradient("#007eff 0%, #007eff 83%, #0f82f8 100%", "dblueG");
addGradient("#d0d0d0 0%, #d0d0d0 100%", "lgrayG");
addGradient("#ff8900 0%, #ff8900 15%, #ffb100 80%, #ffb100 98%, #f9af09 100%", "lorangeG");


//setting up the root
function init(r) {
	//check weather root json or not
	if(r) {
		root = r;
		root.x0 = 0;
		root.y0 = height / 2;
		root.children.forEach(collapse);
		isRootSetupDone = true;
	}else {
		isRootSetupDone = false;
		// console.log("root is null");
	}
}





function update(source) {
	if(!isRootSetupDone) {
		console.log('please setup the root first call  < init(root) > function')
		return;
	}

    // Compute the new tree layout.
    var nodes = tree.nodes(root).reverse(),
        links = tree.links(nodes);

    // Normalize for fixed-depth (update y position)
    nodes.forEach(function (d) {
        d.y = d.depth * 180;
    });

    // Update the nodes…
    var node = svg.selectAll("g.node")
        .data(nodes, function (d) {
        return d.id || (d.id = ++i);
    });

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append("g")
        .attr("class", "node")
        .attr("transform", function (d) {
        return "translate(" + source.x0 + "," + source.y0 + ")";
    });

    //add main nodes
    nodeEnter.append("rect")
    	.attr("id", function(d) {return isTrue(d, serverText) ? d.id_+"-"+d.subDomain : "";})
        .attr("width", function(d) {return isTrue(d, rootText) ? 0 : rectW})
        .attr("height",  function(d) {return isTrue(d, rootText) ? 0 : rectH})
        .attr("stroke", "black")
        .attr("stroke-width", 1)
        .style("fill", function (d) {
        return d._children ? "lightsteelblue" : "#fff";
    });

    //title text
    nodeEnter.append("text")
        .attr("id", "")
        .attr("class", "title")
        .attr("fill", "#fff")
        .attr("x", rectW / 2)
        .attr("y", rectH / 2)
        .attr("dy", "-1em")
        .attr("text-anchor", "middle")
        .text(function(d) {
        return isTrue(d, rootText) ? "" : d.name;
    });
    
    //uptime text
    nodeEnter.append("text")
        .attr("id", "")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .text(function(d) { //return isTrue(d, rootText) || d.node_type == "normal"
        return isTrue(d, rootText) || isTrue(d, moreInfoText) || isTrue(d, patchesText) || isTrue(d, tenantsText) || isTrue(d, endText) || isTrue(d, clusterText)  || isTrue(d, patchText) || d.node_type == "normal" ? "" : "Up Time";
    });
    
    //uptime text value
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, serverText) ? d.id_+"-uptime" : "";})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "2.0em")
    .text(function(d) {
        return isTrue(d, rootText) ? "" : d.uptime;
    });

    //version
    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "2.0em")
    .text(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = "Version";
        }

        if(isTrue(d, endText)) {
            text = "Domain";
        }
        return text;
    });

    //free memory
    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "3.5em")
    .text(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = "Free Memory";
        }

        if(isTrue(d, endText)) {
            text = "Email";
        }
        return text;
    });

    //thread count
    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "5.0em")
    .text(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = "Thread Count";
        }

        if(isTrue(d, endText)) {
            text = "Active";
        }
        return text;
    });

    //load average
    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "6.5em")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? "Load Average" : "";
    });

    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "8.0em")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? "M Console" : "";
    });

//newly add
    nodeEnter.append("text")
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "-5.5em")
        .attr("dy", "9.5em")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? "View log" : "";
    });


    
    //cpu text
    nodeEnter.append("text")
        .attr("class", "title-2")
        .attr("x", rectW/2)
        .attr("y", rectH / 2)
        .attr("dx", "-1.5em")
        .text(function(d) {
        return isTrue(d, rootText) || isTrue(d, moreInfoText) || isTrue(d, patchesText) || isTrue(d, tenantsText) || isTrue(d, endText) || isTrue(d, clusterText)  || isTrue(d, patchText) || d.node_type == "normal" ? "" : "%CPU";
    });
    
    //cpu text value
   cpuValues = nodeEnter.append("text")
        .attr("id", function(d) {
            var id = "";
            if(isTrue(d, serverText)){
                id = d.id_+"-cpu";
            }
            
            return isTrue(d, serverText) ? id : "";})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH / 2)
        .attr("dx", "-1.5em")
        .attr("dy", "2.0em")
        .text(function(d) {
        return isTrue(d, rootText) || isTrue(d, moreInfoText) ? "" : d.cpu;
    });
    
    //round robbin text
    nodeEnter.append("text")
        .attr("class", "title-2")
        .attr("x", rectW/2)
        .attr("y", rectH / 2)
        .attr("dx", "4.5em")
    .attr("fill", function(d) {return isTrue(d, moreInfoText) ? "#fff": "#000"})
        .text(function(d) {
            var txt = "";
            if(isTrue(d, clusterText)) {
                txt = "Round Robbin";
            }
            else if(isTrue(d, serverText)) {
                txt = "Shutdown/Restart";
            }
            else {
                txt = "";
            }
            return txt;
        // return isTrue(d, rootText) || isTrue(d, moreInfoText) || isTrue(d, patchesText) || isTrue(d, tenantsText) || isTrue(d, endText)  || isTrue(d, patchText) ? "" : "Round Robbin";
    });



    //version    
    nodeEnter.append("text")
        .attr("id", function(d) {
            var id = "";
            if(isTrue(d, moreInfoText)) {
                id=d.id_+"-version"
            }

            if(isTrue(d, patchesText)) {
                id=d.id_+"-patch"
            }
            return id;})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "2.0em")
    .html(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = d.version;
        }

        if(isTrue(d, endText)) {
            text = d.domain;
        }
        return text;
    });

    //free memory
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, moreInfoText) ? d.id_+"-memory" : "";})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "3.5em")
    .text(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = d.freeMemory;
        }

        if(isTrue(d, endText)) {
            text = d.email;
        }
        return text;
    });   

    //thread count
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, moreInfoText) ? d.id_+"-thread" : "";})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "5.0em")
    .text(function(d) {
        var text = "";
        if(isTrue(d, moreInfoText)) {
            text = d.threadCount;
        }
        if(isTrue(d, endText)) {
            text = d.active;
        }
        return text;
    });  

    //load average
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, moreInfoText) ? d.id_+"-load" : "";})
        .attr("class", "title-3")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "6.5em")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? d.loadAverage: "";
    }); 

    //login
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, moreInfoText) ? d.id_+"-url" : "";})
        .attr("class", "title-3 login")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "8.0em")
        .attr("fill", "blue")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? "login": "";
    }).on("click", function(d) { window.open(d.url); });;     

    //newly add
    nodeEnter.append("text")
        .attr("id", function(d) {return isTrue(d, moreInfoText) ? d.id_+"-log" : "";})
        .attr("class", "title-3 login")
        .attr("x", rectW/2)
        .attr("y", rectH/2)
        .attr("dx", "4.5em")
        .attr("dy", "9.5em")
        .attr("fill", "blue")
    .text(function(d) {
        return isTrue(d, moreInfoText) ? "view log": "";
    }).on("click",  viewLog);         

    
    
    //add restart icon
    nodeEnter.append("rect")
        .attr("id", function(d) {return d.id_;})
        .attr("class", "icon-restart")
        .attr("width", iconW)
        .attr("height", iconH)
        .attr("rx", 5)
        .attr("ry", 5)
        .attr("transform", "translate("+(rectW-iconW-padding/2)+", "+(rectH-iconH-padding/2)+")")
        .attr("stroke", function(d) {return isTrue(d, rootText) || d.node_type == "normal" ? "none" : "#fff";})
        .attr("stroke-width", 1.2)
    .style("fill", function(d) {return isTrue(d, rootText) || d.node_type == "normal" ? "none": "url(#lblackG)" })
        .on("click", restart); ;
    
    //add shutdown icon
    nodeEnter.append("rect")
        .attr("id", "")
        .attr("class", "icon-shutdown")
        .attr("width", iconW)
        .attr("height", iconH)
        .attr("rx", 5)
        .attr("ry", 5)
        .attr("transform", "translate("+(rectW-iconW*2-padding)+", "+(rectH-iconH-padding/2)+")")
    .attr("stroke", function(d) {return isTrue(d, rootText) || d.node_type == "normal" ? "none" : "#fff";})
        .attr("stroke-width", 1.2)
    .style("fill", function(d) {return isTrue(d, rootText) || d.node_type == "normal" ? "none": "url(#lblackG)" })
        .on("click", shutdown); ;
    
     

    // Transition nodes to their new position.
    var nodeUpdate = node.transition()
        .duration(duration)
        .attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
    });
    
    //restart text
    nodeEnter.append('text')
    .attr("id", "")
    .attr("class", "icon-restart")
    .attr('font-family', 'FontAwesome')
    .attr('font-size', function(d) { return 1+'em'} )
    .text(function(d) { return isTrue(d, rootText) || d.node_type == "normal"  ? '' : "\uf01e" })
    .attr("transform", "translate("+(rectW-iconW+padding*0.2)+", "+(rectH-iconH/2+padding*0.1)+")")
    .on("click", restart); ; 
    
    //shutdown text
    nodeEnter.append('text')
    .attr("id", "")
    .attr("class", "icon-shutdown")
    .attr('font-family', 'FontAwesome')
    .attr('font-size', function(d) { return 1+'em'} )
    .text(function(d) { return isTrue(d, rootText) || d.node_type == "normal" ? '' : "\uf011"})
    .attr("transform", "translate("+(rectW-iconW*2-padding*0.2)+", "+(rectH-iconH/2+padding*0.1)+")")
    .on("click", shutdown);
    
    //add drop down icon (rect)
    nodeEnter.append("rect")
        .attr("id", "")
        .attr("class", "icon-dropdown")
        .attr("width", iconH)
        .attr("height", iconH)
        .attr("rx", 5)
        .attr("ry", 5)
        .style("stroke", function (d) {
        return isTrue(d, rootText) || isTrue(d, patchText) || isTrue(d, endText) ? "none" : "#fff";
    })
        .attr("stroke-width", 1.2)
        .attr("transform", "translate("+(padding/2*-1.2)+", "+(rectH/2-padding)+")")
        .style("fill", function(d) {return isTrue(d, rootText) || isTrue(d, patchText) || isTrue(d, endText)? "none": "url(#lblackG)" })
        .on("click", dropdown); 

    //add drop down icon (plus)
    nodeEnter.append('text')
        .attr("class", "icon-dropdown")
        .attr('font-family', 'FontAwesome')
        .attr('font-size', function(d) { return 1+'em'} )
        .text(function(d) { 
            var icon = "\uf067";
            if(d.name.indexOf(serverText) > -1) {
                icon = "  \uf129";
            }
            return icon })
        .style("fill", function (d) {
        return isTrue(d, rootText) || isTrue(d, patchText) || isTrue(d, endText)? "none" : "#fff";
    })
        .attr("transform", "translate("+(padding/2*-0.5)+", "+(rectH/2+padding/1.8)+")")
        .on("click", dropdown);     
    

//update size of nodes
    nodeUpdate.select("rect")
    .attr("width", function(d) {
        var w = rectW;
        if(isTrue(d, rootText)) {
            w = rectW;
        }

        if(isTrue(d, endText)) {
            w = rectW + 5;
        }

        return w;})
        .attr("height",  function(d) {
            var h = 0;
            if(isTrue(d, moreInfoText)) {
                h = rectH * 2.0; //newly add
            }
            else if(isTrue(d, endText)) {
                h = rectH * 1.5;
            }
            else if(isTrue(d, patchesText) || isTrue(d, tenantsText) || isTrue(d, patchText) || d.node_size == "small") {
                h = rectH/2;
            }
            else{
                h = rectH;
            }
            return isTrue(d, rootText) ? 0 : h})
        .attr("rx", 6)
        .attr("ry", 6)
        .attr("stroke", "black")
        .attr("stroke-width", 1)
        .style("fill", function (d) {
            var color = "url(#dblueG)";

            if(isTrue(d, clusterText)) {
                color = "url(#lorangeG)";
            }

            if(isTrue(d, serverText)) {
                if(d.subDomain == "mgt") {
                    color = "url(#dpurpleG)";
                }else{
                    color = "url(#dblueG)";
                }
            }
            
            if(isTrue(d, moreInfoText)) {
                color = "url(#lblueG)"; 
            }

            /*if(isTrue(d, endText)) {
                if(d.active == 'false'){
                    color = "#FF1300"; 
                }
                //console.log(d.active)
            }*/

            if(isTrue(d, tenantsText) || isTrue(d, endText)) {

                color = "#8E8E93"; 

                if(d.active == 'false'){
                    color = "#FF1300"; 
                }
            }

            if(isTrue(d, patchesText) || isTrue(d, patchText)) {
                color = "#5856D6"; 
            }

            


            
        return color;
    });

    nodeUpdate.select("text")
        .style("fill-opacity", 1);

    // Transition exiting nodes to the parent's new position.
    var nodeExit = node.exit().transition()
        .duration(duration)
        .attr("transform", function (d) {
        return "translate(" + source.x + "," + source.y + ")";
    })
        .remove();

    nodeExit.select("rect")
        .attr("width", rectW)
        .attr("height", rectH)
        .attr("rx", 6)
        .attr("ry", 6)
    //.attr("width", bbox.getBBox().width)""
    //.attr("height", bbox.getBBox().height)
        .attr("stroke", "black")
        .attr("stroke-width", 1);

    nodeExit.select("text");

    // Update the links…
    var link = svg.selectAll("path.link")
        .data(links, function (d) {
        return d.target.id;
    });

    // Enter any new links at the parent's previous position.
    link.enter().insert("path", "g")
        .attr("class", "link")
        .attr("x", rectW / 2)
        .attr("y", rectH / 2)
    .attr("stroke", function(d) {
        var color = "#000";
        if(isTrue(d.source, rootText) && isTrue(d.target, clusterText)) {
            color = "none";
        }
        
        return color;
    })
        .attr("d", function (d) {
            
        var o = {
            x: source.x0,
            y: source.y0
        };
        return diagonal({
            source: o,
            target: o
        });
    });

    // Transition links to their new position.
    link.transition()
        .duration(duration)
        .attr("d", diagonal);

    // Transition exiting nodes to the parent's new position.
    link.exit().transition()
        .duration(duration)
        .attr("d", function (d) {
        var o = {
            x: source.x,
            y: source.y
        };
        return diagonal({
            source: o,
            target: o
        });
    })
        .remove();

    // Stash the old positions for transition.
    nodes.forEach(function (d) {
        d.x0 = d.x;
        d.y0 = d.y;
    });
}

// Toggle children on click.
function dropdown(d) {
    if (d.children) {
        d._children = d.children;
        d.children = null;
    } else {
        d.children = d._children;
        d._children = null;
    }
    update(d);
}

//newly add
function viewLog(d) {
    var cid = d.parent.domain;
    var nid = d.id_.replace("n", "");
    publish(cid, nid);
}

function shutdown(d){
    // console.log("shutdown");
    var nodes = [];
    var commands = [];


    var check = d.id_[d.id_.length - 1]
    
    if(!hasNumber(check)) { 

        if(d.children){            
            nodes.push(d.children)

            for (var i = 0; i < d.children.length; i++) {
                var c = d.children[i];
                var node = {
                    nodeId : c.id_,
                    clusterId : c.domain
                }
                commands.push(node)
            }
        }

        if(d._children) {
            for (var i = 0; i < d._children.length; i++) {
                var c = d._children[i];
                var node = {
                    nodeId : c.id_,
                    clusterId : c.domain
                }
                commands.push(node)
            }
        }
        
        
    }else{
        var node = {
            nodeId : d.id_,
            clusterId : d.domain
        }
        commands.push(node);
    }
    // console.log(commands)

    confirmCommandDialogBox("Shutdown", api, commands);

}

function restart(d){
    // console.log("restart----");
    //console.log(d);
    var nodes = [];
    var commands = [];


    var check = d.id_[d.id_.length - 1]
    
    if(!hasNumber(check)) { 

    	if(d.children){            
    		nodes.push(d.children)

    		for (var i = 0; i < d.children.length; i++) {
    			var c = d.children[i];
                var node = {
                    nodeId : c.id_,
                    clusterId : c.domain
                }
    			commands.push(node)
    		}
    	}

    	if(d._children) {
    		for (var i = 0; i < d._children.length; i++) {
    			var c = d._children[i];
    			var node = {
                    nodeId : c.id_,
                    clusterId : c.domain
                }
                commands.push(node)
    		}
    	}
    	
    	
    }else{
        var node = {
            nodeId : d.id_,
            clusterId : d.domain
        }
        commands.push(node);
    }
    // console.log(commands)

    confirmCommandDialogBox("Restart", api, commands);
    
}

function updateCpuValues() {
	// console.log('up....')
	var x = svg.selectAll("text");
	// console.log(x)

}


function hasNumber(s) {
  return /\d/.test(s);
}

function isTrue(d, compare) {
    // console.log(d)
    return (d.name.indexOf(compare) > -1)
}

//Redraw for zoom
function redraw() {
  //console.log("here", d3.event.translate, d3.event.scale);
  svg.attr("transform",
      "translate(" + d3.event.translate + ")"
      + " scale(" + d3.event.scale + ")");
}

//gradient function
function addGradient(g, id) {
    var gradient = svg.append("svg:defs")
    .append("svg:linearGradient")
    .attr("id", id)
    .attr("x1", "0%")
    .attr("y1", "0%")
    .attr("x2", "100%")
    .attr("y2", "100%")
    .attr("spreadMethod", "pad");
    var cols = g.split(", ");
    //console.log(info)
    // Define the gradient colors
    cols.map(function (c) {
        var info = c.split(" ");
        //console.log(info)
        //get color info
        gradient.append("svg:stop")
            //percentage
            .attr("offset", info[1].trim())
            //color hex
            .attr("stop-color", info[0].trim())
            .attr("stop-opacity", 1);
    });
}



/*js testing*/

//make title to upper case

function test(){ 
    var titles = $('#https1010041139458title-3');
    titles.map(function(t) {
        //console.log()
        var txt = $(titles[t]).text().toUpperCase();
         $(titles[t]).text(Math.random()*100);
    });
}

function confirmCommandDialogBox(title, api, commands) {
    $("#dialog-confirm").html("Do you really want to " + title.toLowerCase() + "!");
    var commandId = 0;
    // console.log("-----------")
    // console.log(title.toLowerCase())
    // console.log(commands)

    // Define the Dialog and its properties.
    $("#dialog-confirm").dialog({
        resizable: false,
        modal: true,
        title: title,
        height: 170,
        width: 350,
        buttons: {
            "Forced": function () {
                if(title.toLowerCase().trim() == 'restart') {
                    commandId = "FORCE_RESTART";
                }else {
                    commandId = "FORCE_SHUTDOWN";
                }
                $(this).dialog('close');
                callback(commandId, api, commands);
            },
            "Graceful": function () {
                if(title.toLowerCase().trim() == 'restart') {
                    commandId = "GRACEFUL_RESTART";
                }else {
                    commandId = "GRACEFUL_SHUTDOWN";
                }
                $(this).dialog('close');
                callback(commandId, api, commands);
            },
            "Cancel": function () {
                $(this).dialog('close');
            }
        }
    });
}

function callback(commandId, api, commands) {
   

    // console.log(commands.length)
    // console.log(commands)
    //node command
    if(commands.length == 1) {
        // $.post(api, commands[0].nodeId+","+commands[0].clusterId+","+commandId, 'text'); 
        $.post(api+"?cid="+commands[0].clusterId+"&nid="+commands[0].nodeId.replace("n", "")+"&command="+commandId, 'text'); 
    }
    //cluster command
    else {
        // $.post(api, ""+","+commands[0].clusterId+","+commandId, 'text'); 
        $.post(api+"?cid="+commands[0].clusterId+"&nid=null&command="+commandId, 'text'); 
    }

    //cluster command

    //$.post(api, commands[0].nodeId+","+commands[0].clusterId+","+commandId, 'text'); 
}

//setInterval(test, 100);





