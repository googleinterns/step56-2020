
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
				messageElement.id = `msg${pos}`
				messageElement.innerText = msg.message + " from " + msg.writer;
				messageChain.appendChild(messageElement);
				currentMessages.push(msg);
			} else if (currentMessages[pos] == msg) {
				continue;
			} else {
				var messageElement;
				messageElement = document.getElementById(`msg${pos}`);
				messageElement.innerText = msg.message + " from " + msg.writer;

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
}, 1 * 30 * 1000);
