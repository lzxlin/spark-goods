var singledata = {
    url:{
        getData:"/getSingleFollowData"
    },
    fn:{
        renderMain:function(){
            //ajax的回调函数
            function callback(data) {
                if(data.success){
                    //alert("start");
                    singledata.fn.drawChart(data);
                }else{
                    alert("错误："+data.msg)
                }
            }
            var params={};
            singledata.fn.ajax(singledata.url.getData,params,callback)
        },
        drawChart:function(data){
            var datelist=[],valuelist=[];
            var datas=data.data;
            var k=0;
            for(var i=0;i<datas.length;i++){
                datelist.push(k);
                k+=5;
                valuelist.push(datas[i].windowFollowValue);
            }
            option = {

                // Make gradient line here
                visualMap: [{
                    show: false,
                    type: 'continuous',
                    seriesIndex: 0,
                    min: 0,
                    max: 8
                }, {
                    show: false,
                    type: 'continuous',
                    seriesIndex: 1,
                    dimension: 0,
                    min: 0,
                    max: datelist.length-1
                }],


                title: [{
                    left: 'center',
                    text: '折线图'
                }, {
                    top: '55%',
                    left: 'center',
                    text: '曲线图'
                }],
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: [{
                    data: datelist
                }, {
                    data: datelist,
                    gridIndex: 1
                }],
                yAxis: [{
                    splitLine: {show: false}
                }, {
                    splitLine: {show: false},
                    gridIndex: 1
                }],
                grid: [{
                    bottom: '60%'
                }, {
                    top: '60%'
                }],
                series: [{
                    type: 'line',
                    showSymbol: false,
                    data: valuelist
                }, {
                    type: 'bar',
                    showSymbol: false,
                    data: valuelist,
                    xAxisIndex: 1,
                    yAxisIndex: 1
                }]
            };
            var image = echarts.init(document.getElementById("singleValueChart"),true);
            image.setOption(option)
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
};