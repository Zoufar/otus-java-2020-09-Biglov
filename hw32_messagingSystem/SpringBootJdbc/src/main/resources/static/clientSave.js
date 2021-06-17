let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {    } else {    }
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/clientSaved', (message) => showMessage(JSON.parse(message.body)));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => {
    const form = document.getElementById('form');
    const name = form.querySelector('[name="name"]'), //получаем поле name
        login = form.querySelector('[name="login"]'), //получаем поле age
        password = form.querySelector('[name="password"]'); //получаем поле terms
    const client = {
        id: 0,
        name: name.value,
        login: login.value,
        password: password.value};
    console.log(client);
    stompClient.send("/app/client", {}, JSON.stringify(client));
    $("#form")[0].reset();;
}

const showMessage = (message) => {
    const row = document.createElement('tr');
    row.innerHTML = "<td>"+message.id+"</td> <td>"+message.name+"</td><td>"+message.login+"</td> <td>"+message.password+"</td>";
    document.querySelector('.client').innerHTML="";
    document.querySelector('.client').appendChild(row);
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendMsg);
});
