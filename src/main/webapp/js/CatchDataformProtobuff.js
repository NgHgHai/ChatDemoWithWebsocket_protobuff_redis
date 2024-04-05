const protobuf = require("protobufjs");
function toChatMessageProto(payload) {

}

protobuf.load("mess.proto",function (error, root) {
    if (error) throw error;

    var chatmess = root.lookupType("ChatMessage")
    var payload ={
        date :"23/3",
        name :"hoang hai",
        mess :"tao la hai ne"
    }
    var errMsg = chatmess.verify(payload);
    if (errMsg) throw Error(errMsg);
    var message = chatmess.create(payload);

    console.log(message)
})
