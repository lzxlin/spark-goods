var evalchart = {
    url:{
        getData:"/getEvalData"
    },
    fn:{
        renderMain:function(){
            //ajax的回调函数
            function callback(data) {
                if(data.success){
                    //alert("start");
                    evalchart.fn.drawEvalChart1(data);
                    evalchart.fn.drawEvalChart2(data);
                    evalchart.fn.drawEvalChart3(data);
                    //bigdata.fn.drawCustomizedPieChart(data);
                    //alert("end");
                }else{
                    alert("错误："+data.msg)
                }
            }
            var params={};
            evalchart.fn.ajax(evalchart.url.getData,params,callback)
        },
        drawEvalChart1:function(data){

            var datas=data.data;
            var rank=[],rmse=[],time=[];
            var k=0;
            for(var i=0;i<datas.length;i++){
               if(datas[i].iterations==10&&datas[i].lambda==0.1){
                   k++;
                   var ranks=datas[i].rank;
                   var rmses=datas[i].rmse;
                   var times=datas[i].time;
                   rank.push(ranks);
                   rmse.push(rmses);
                   time.push(times);
                   if(k==6) break;
               }
            }

            option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data:['均方误差','训练时间']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: rank,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '均方误差',
                        min: 0,
                        max: 3.5,
                        interval: 0.5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    },
                    {
                        type: 'value',
                        name: '训练时间',
                        min: 0,
                        max: 20000,
                        interval: 2000,
                        axisLabel: {
                            formatter: '{value} ms'
                        }
                    }
                ],
                series: [
                    {
                        name:'均方误差',
                        type:'bar',
                        data:rmse
                    },
                    {
                        name:'训练时间',
                        type:'line',
                        yAxisIndex: 1,
                        data:time
                    }
                ]
            };
            //alert(document.getElementById("followChart1"));
            var image = echarts.init(document.getElementById("evalChart1"),true);
            image.setOption(option)
        },
        drawEvalChart2:function(data){

            var datas=data.data;
            var iterations=[],rmse=[],time=[];
            for(var i=6;i<11;i++){
                    var iteration=datas[i].iterations;
                    var rmses=datas[i].rmse;
                    var times=datas[i].time;
                    iterations.push(iteration);
                    rmse.push(rmses);
                    time.push(times);
            }

            option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data:['均方误差','训练时间']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: iterations,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '均方误差',
                        min: 0,
                        max: 3.5,
                        interval: 0.5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    },
                    {
                        type: 'value',
                        name: '训练时间',
                        min: 0,
                        max: 20000,
                        interval: 2000,
                        axisLabel: {
                            formatter: '{value} ms'
                        }
                    }
                ],
                series: [
                    {
                        name:'均方误差',
                        type:'bar',
                        data:rmse
                    },
                    {
                        name:'训练时间',
                        type:'line',
                        yAxisIndex: 1,
                        data:time
                    }
                ]
            };
            //alert(document.getElementById("followChart1"));
            var image = echarts.init(document.getElementById("evalChart2"),true);
            image.setOption(option)
        },
        drawEvalChart3:function(data){

            var datas=data.data;
            var lambdas=[],rmses=[],times=[];
            for(var i=11;i<16;i++){
                var lambda=datas[i].lambda;
                var rmse=datas[i].rmse;
                var time=datas[i].time;
                //alert(lambda+" "+rmse+" "+time)
                lambdas.push(lambda);
                rmses.push(rmse);
                times.push(time);
            }

            option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data:['均方误差','训练时间']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: lambdas,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '均方误差',
                        min: 0,
                        max: 3.5,
                        interval: 0.5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    },
                    {
                        type: 'value',
                        name: '训练时间',
                        min: 0,
                        max: 20000,
                        interval: 2000,
                        axisLabel: {
                            formatter: '{value} ms'
                        }
                    }
                ],
                series: [
                    {
                        name:'均方误差',
                        type:'bar',
                        data:rmses
                    },
                    {
                        name:'训练时间',
                        type:'line',
                        yAxisIndex: 1,
                        data:times
                    }
                ]
            };
            //alert(document.getElementById("followChart1"));
            var image = echarts.init(document.getElementById("evalChart3"),true);
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