let messageElem = document.getElementById("message");
let userElem = document.getElementById("username");
let messagesBox = document.getElementById("messages");
let submitButton = document.getElementById("submit");
let ws = new WebSocket("ws://localhost:8080");

submitButton.addEventListener("click", sendMessage);

function sendMessage() {
    let user = userElem.value;
    let message = messageElem.value;
    ws.send(user + ": " + message);
    messageElem.value = "";
}

function publishMessage(event) {
    let newMessage = document.createElement("p");
    newMessage.textContent = event.data + "\n";
    messagesBox.appendChild(newMessage);
}
ws.onmessage = publishMessage;


