
var currentMessages = [];

// Display user's favorite restaurants in "Favorites" bar
function displayMessageChain() {
	fetch(`/chat?recipient=${currentStore}`).then(response => response.json()).then((messages) => {
		var messageChain = document.getElementById('message-chain');
		var pos = 0;
		var test = true;
		for (const msgPosition in messages) {
			const msg = messages[msgPosition];
			if (currentMessages.length <= pos && document.getElementById(`msg${pos}`) == null) {
				var messageElement;
				messageElement = document.createElement("li");
				messageElement.className = "msg";
				messageElement.id = `msg${pos}`;
				messageElement.innerText = msg.message + " from " + (msg.writer.search("@") > -1 ? "You" : "Store"); 
				messageChain.appendChild(messageElement);
				currentMessages.push(msg);
			} else {
				var messageElement;
				messageElement = document.getElementById(`msg${pos}`);
				messageElement.className = "msg";
				messageElement.innerText = msg.message + " from " + (msg.writer.search("@") > -1 ? "You" : "Store"); 

			}
			pos = pos+1;
		}
		while (test) {
			messageElement = document.getElementById(`msg${pos}`);
			if (messageElement == null) {
				test = false;
			} else {
				messageChain.removeChild(messageElement);
			}
			pos = pos+1;
		}
	});
}

function sendMessage() {
	message = document.getElementById("message").value;
	var oReq = new XMLHttpRequest();
	oReq.open("POST", "/chat");
	oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	oReq.send(`recipient=${currentStore}&message=${message}&timestamp=${new Date().getTime()}`);
	displayMessageChain();
}

setInterval(async function(){
	if (currentStore != null) {
		displayMessageChain()
	}
}, 1 * 1 * 1000);

URLid = window.location.href.split("?")[1].split("&")[1].split("=")[1];
URLname = window.location.href.split("?")[1].split("&")[2].split("=")[1];
if (URLid != "") {
	selectMarker(URLid, URLname);
}
