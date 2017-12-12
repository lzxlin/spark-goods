var userRdata = {
    url:{
        getData:"/getUserData"
    },
    fn:{
        showUserRDataTable:function () {
            var result="";
            $.ajax({
                contentType : 'application/json',
                //请求方式
                type: "POST",
                //请求的url
                url: userRdata.url.getData,
                //参数
                data: {},
                //cache:false,
                async: false,//同步  ,
                //ajax成功
                success: function (data) {
                    //成功的回调函数
                    //alert("success");
                    result=userRdata.fn.returnData(data);
                    //alert(result);
                },
                error: function (XMLHttpRequest,data) {
                    //错误的回调函数
                    alert(data);
                }
            })
            return result;
        },
        returnData:function (data) {
            //alert(typeof id);
            var datas=data.data;
            var text="";
            for(var i=0;i<datas.length;i++){
                var str="<tr>" +
                    "<td>"+datas[i].userID+"</td>" +
                    "<td>"+datas[i].itemID+"</td>"+
                    "<td>"+datas[i].scores+"</td>"+
                    "</tr>";
                text+=str;
            }
            //alert("text"+text);
            return text;
        },
        searchUserRDataTable:function (value) {
            var result="";
            $.ajax({
                contentType : 'application/json',
                //请求方式
                type: "POST",
                //请求的url
                url: userRdata.url.getData,
                //参数
                data: {},
                //cache:false,
                async: false,//同步  ,
                //ajax成功
                success: function (data) {
                    //成功的回调函数
                    //alert("success");
                    var datas=data.data;
                    var text="";
                    for(var i=0;i<datas.length;i++){
                        if(parseInt(datas[i].userID)==parseInt(value)){
                            text+="<tr>" +
                                "<td>"+datas[i].userID+"</td>" +
                                "<td>"+datas[i].itemID+"</td>"+
                                "<td>"+datas[i].scores+"</td>"+
                                "</tr>";
                        }
                    }
                    result=text;
                },
                error: function (XMLHttpRequest,data) {
                    //错误的回调函数
                    alert(data);
                }
            });
            return result;
        }
    }
};