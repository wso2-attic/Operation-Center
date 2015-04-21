// $(function() {
// 	setTimeout(publish, 4000);
// });


/* This defines the publish() function call that will get triggered when the latter mentioned button is clicked.  */
function publish(cid, nid) {
	console.log("called.....")
        // var message = Math.random();
        var message = {
        	cid : cid,
        	nid : nid
        }
        /* This defines that a random number needs to be published to my-channel, which has been declared in the ModulePrefs section */
        gadgets.Hub.publish("oc-channel", message);
        // console.log(message)
        // document.getElementById("output").innerHTML = "From the oc channel : " + message;
}

