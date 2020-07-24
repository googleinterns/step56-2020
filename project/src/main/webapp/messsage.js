
// Display user's favorite restaurants in "Favorites" bar
function displayMessageChain() {
	fetch('/chat').then(response => response.json()).then((messages) => {
		var messageChain = document.getElementById('message-chain');
		for (const message in messages {
			var messageElement = document.createElement("li");
			messageElement.innerText = message.message + " from " + message.writer;
			messageChain.appendChild(messageElement);
		}
	});
}

function sendMessage() {
	message = document.getElementById("message");
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/message");
        oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        oReq.send(`recipient=${"store"}&message=${message}&timestamp=${new Date().getTime()}`);
}

setInterval(async function(){
	//add compare messages before displaying to prevent constant reloading
	displayMessageChain()
}, 1 * 30 * 1000);
