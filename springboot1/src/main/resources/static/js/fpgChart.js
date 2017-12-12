var fpgchart = {
    url:{
        getData:"/getFpgData"
    },
    fn:{
        renderMain:function(){
            //ajax的回调函数
            function callback(data) {
                if(data.success){
                    //alert("start");
                    fpgchart.fn.drawEvalChart1(data);
                    fpgchart.fn.drawEvalChart2(data);
                    //alert("end");
                }else{
                    alert("错误："+data.msg)
                }
            }
            var params={};
            fpgchart.fn.ajax(fpgchart.url.getData,params,callback)
        },
        drawEvalChart1:function(data){

            var datas=data.data;
            var item=[],category=[],link=[],center=[],dictData=[];
            for(var i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                //alert(front+" "+after);
                for(var j=0;j<front.length;j++){
                    var s=$.inArray(front[j],item);
                    //alert(front[j]+" "+s);
                    if(s==-1){
                        //alert("success");
                        item.push(front[j]);
                    }
                }
                var ss=$.inArray(after,center);
                if(ss==-1){
                    center.push(after);
                }
            }
            for(i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                for(j=0;j<front.length;j++){
                    var dict={};
                    if($.inArray(front[j],center)!=-1){
                        dict={
                            name:front[j],
                            category:1,
                            draggable: true
                        };
                    }else{
                        dict={
                            name:front[j],
                            category:0,
                            draggable: true
                        };
                    }
                    for(var k=0;k<dictData.length;k++){
                        if(dict.name==dictData[k].name){
                            break;
                        }
                    }
                    if(k==dictData.length){
                        dictData.push(dict);
                    }
                }
                dict={
                    name:after,
                    category:1,
                    draggable: true
                };
                for(k=0;k<dictData.length;k++){
                    if(dict.name==dictData[k].name){
                        break;
                    }
                }
                if(k==dictData.length){
                    dictData.push(dict);
                }
            }
            /*for(i=0;i<dictData.length;i++){
                alert(dictData[i].name+" "+dictData[i].category);
            }*/
            //求links
            for(i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                for(j=0;j<front.length;j++){
                    var dict={
                        source: front[j],
                        target: after,
                        value: datas[i].confidence
                    };
                    link.push(dict);
                }
            }
            /*for(i=0;i<link.length;i++){
                alert(link[i].source+" "+link[i].target+" "+link[i].value);
            }*/
            option = {
                title: {
                    text:''
                },
                tooltip: {},
                animationDurationUpdate: 1500,
                animationEasingUpdate: 'quinticInOut',
                label: {
                    normal: {
                        show: true,
                        textStyle: {
                            fontSize: 12
                        },
                    }
                },
                legend: {
                    x: "center",
                    show: false,
                    //data: ["朋友", "战友", '亲戚']
                },
                series: [

                    {
                        type: 'graph',
                        layout: 'force',
                        symbolSize: 45,
                        focusNodeAdjacency: true,
                        roam: true,
                        categories: [{
                            name: '前项',
                            itemStyle: {
                                normal: {
                                    color: "#009800",
                                }
                            }
                        }, {
                            name: '后项',
                            itemStyle: {
                                normal: {
                                    color: "#ff411c",
                                }
                            }
                        }],
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    fontSize: 12
                                }
                            }
                        },
                        force: {
                            repulsion: 1000
                        },
                        edgeSymbolSize: [4, 50],
                        edgeLabel: {
                            normal: {
                                show: true,
                                textStyle: {
                                    fontSize: 10
                                },
                                formatter: "{c}"
                            }
                        },
                        data:dictData,
                        links: link,
                        lineStyle: {
                            normal: {
                                opacity: 0.9,
                                width: 1,
                                curveness: 0
                            }
                        }
                    }
                ]
            };
            var image = echarts.init(document.getElementById("fpgChart1"),true);
            image.setOption(option)
        },
        drawEvalChart2:function(data){

            var datas=data.data;
            var item=[],category=[],link=[],center=[],dictData=[];
            for(var i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                //alert(front+" "+after);
                for(var j=0;j<front.length;j++){
                    var s=$.inArray(front[j],item);
                    //alert(front[j]+" "+s);
                    if(s==-1){
                        //alert("success");
                        item.push(front[j]);
                    }
                }
                var ss=$.inArray(after,center);
                if(ss==-1){
                    center.push(after);
                }
            }
            for(i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                for(j=0;j<front.length;j++){
                     var dict={
                        name:i+"#"+front[j],
                        category:0,
                        draggable: true
                    };
                     dictData.push(dict);
                }
                dict={
                    name:i+"#"+after,
                    category:1,
                    draggable: true
                };
               dictData.push(dict);
            }
            for(i=0;i<datas.length;i++){
                var front=datas[i].antecedent.split(" ");
                var after=datas[i].consequent;
                for(j=0;j<front.length;j++){
                    var dict={
                        source: i+"#"+front[j],
                        target: i+"#"+after,
                        value: datas[i].confidence
                    };
                    link.push(dict);
                }
            }
            /*for(i=0;i<link.length;i++){
                alert(link[i].source+" "+link[i].target+" "+link[i].value);
            }*/
            option = {
                title: {
                    text:''
                },
                tooltip: {},
                animationDurationUpdate: 1500,
                animationEasingUpdate: 'quinticInOut',
                label: {
                    normal: {
                        show: true,
                        textStyle: {
                            fontSize: 12
                        },
                    }
                },
                legend: {
                    x: "center",
                    show: false,
                },
                series: [

                    {
                        type: 'graph',
                        layout: 'force',
                        symbolSize: 45,
                        focusNodeAdjacency: true,
                        roam: true,
                        categories: [{
                            name: '前项',
                            itemStyle: {
                                normal: {
                                    color: "#009800",
                                }
                            }
                        }, {
                            name: '后项',
                            itemStyle: {
                                normal: {
                                    color: "#ff411c",
                                }
                            }
                        }],
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    fontSize: 12
                                }
                            }
                        },
                        force: {
                            repulsion: 1000
                        },
                        edgeSymbolSize: [4, 50],
                        edgeLabel: {
                            normal: {
                                show: true,
                                textStyle: {
                                    fontSize: 10
                                },
                                formatter: "{c}"
                            }
                        },
                        data:dictData,
                        links: link,
                        lineStyle: {
                            normal: {
                                opacity: 0.9,
                                width: 1,
                                curveness: 0
                            }
                        }
                    }
                ]
            };
            var image = echarts.init(document.getElementById("fpgChart2"),true);
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
        },
    }
};