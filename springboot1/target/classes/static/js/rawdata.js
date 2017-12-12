var rawdata = {
    url:{
        getData:"/getRawData"
    },
    fn:{
        test:function () {
            var id=document.getElementById("rawTable");
            //alert(typeof id);
            var text="";
            for(var i=0;i<4;i++){
                var str="<tr>" +
                    "<td>"+1+"</td>" +
                    "<td>"+2+"</td>" +
                    "<td>"+3+"</td>" +
                    "<td>"+4+"</td>"+
                    "<td>"+5+"</td>"+
                    "<td>"+6+"</td>"+
                    "<td>"+7+"</td>"+
                    "</tr>";
                text+=str;
                //alert(str);
            }
            id.innerHTML=text;
        },
        renderMain:function(){
            //ajax的回调函数
            var result="";
            $.ajax({
                contentType : 'application/json',
                //请求方式
                type: "POST",
                //请求的url
                url: "/getRawData",
                //参数
                data: {},
                //cache:false,
                async: false,//同步  ,
                //ajax成功
                success: function (data) {
                    //成功的回调函数
                    //alert("success");
                    result=rawdata.fn.showTable(data);
                    //alert(result);
                },
                error: function (XMLHttpRequest,data) {
                    //错误的回调函数
                    alert(data);
                }
            })
            return result;
        },
        showTable:function (data) {
            //alert(typeof id);
            var datas=data.data;
            var text="";
            for(var i=0;i<datas.length;i++){
                var str="<tr>" +
                    "<td>"+datas[i].userID+"</td>" +
                    "<td>"+datas[i].itemID+"</td>" +
                    "<td>"+datas[i].browser_num+"</td>" +
                    "<td>"+datas[i].stay_time+"</td>"+
                    "<td>"+datas[i].collect+"</td>"+
                    "<td>"+datas[i].buy_num+"</td>"+
                    "<td>"+datas[i].scores+"</td>"+
                    "</tr>";
                text+=str;
            }
            //alert("text"+text);
            return text;
        },
        ajax:function(url,parameters,successCallBackFun){
            $.ajax({
                contentType : 'application/json',
                //请求方式
                type: "POST",
                //请求的url
                url: url,
                //参数
                data: parameters,
                cache:false,
                //ajax成功
                success: function (data) {
                    //成功的回调函数
                    successCallBackFun(data);
                },
                error: function (XMLHttpRequest,data) {
                    //错误的回调函数
                    alert(data);
                }
            });
        }
    }
}