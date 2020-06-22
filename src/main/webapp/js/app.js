$("#A").click(function(){
    //模拟30000个异步请求，进行并发
    var max = 30000;
    for (var i = 1; i <= max; i++) {
        show("post","../userRedPacket/grapRedPacket.do",{redPacketId:1,userId: i});
    }
});

$("#B").click(function(){
    //模拟30000个异步请求，进行并发
    var max = 30000;
    for (var i = 1; i <= max; i++) {
        show("post","../userRedPacket/grapRedPacketForVersion.do",{redPacketId:1,userId: i});
    }
});

$("#C").click(function(){
    //模拟30000个异步请求，进行并发
    var max = 30000;
    for (var i = 1; i <= max; i++) {
        show("post","../userRedPacket/grapRedPacketByRedis.do",{redPacketId:1,userId: i});
    }
});

var show = function (methods,url,param) {
    if (methods == "post"){
        $.post(url ,param ,function (data) {
                //$("msg").text(data);
            }
        );
    } else if (methods == "get"){
        $.get(url ,param ,function (data) {
                //$("msg").text(data);
            }
        );
    }
}