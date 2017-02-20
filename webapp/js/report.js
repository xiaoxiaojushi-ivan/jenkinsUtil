$(document).ready(function() {
    lineChart();
});
function lineChart() {
    var data = {
        // labels 数据包含依次在X轴上显示的文本标签
        labels : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
                "十一月", "十二月" ],
        datasets : [ {
            // 数据集名称，会在图例中显示
            label : "红队",

            // 颜色主题，可以是'#fff'、'rgb(255,0,0)'、'rgba(255,0,0,0.85)'、'red' 或
            // ZUI配色表中的颜色名称
            // 或者指定为 'random' 来使用一个随机的颜色主题
            // color : "#BD7B46",
            // 也可以不指定颜色主题，使用下面的值来分别应用颜色设置，这些值会覆盖color生成的主题颜色设置
            fillColor : "rgba(220,220,220,0.2)",
            strokeColor : "rgba(220,220,220,1)",
            pointColor : "rgba(220,220,220,1)",
            pointStrokeColor : "#fff",
            pointHighlightFill : "#fff",
            pointHighlightStroke : "rgba(220,220,220,1)",

            // 数据集
            data : [ 65, 59, 80, 81, 56, 55, 40, 44, 55, 70, 30, 40 ]
        }, {
            label : "绿队",
            // color : "green",
            fillColor : "rgba(151,187,205,0.2)",
            strokeColor : "rgba(151,187,205,1)",
            pointColor : "rgba(151,187,205,1)",
            pointStrokeColor : "#fff",
            pointHighlightFill : "#fff",
            pointHighlightStroke : "rgba(151,187,205,1)",
            data : [ 28, 48, 40, 19, 86, 27, 90, 60, 30, 44, 50, 66 ]
        } ]
    };

    var options = {
        bezierCurve : true,
        responsive : true
    }; // 图表配置项，可以留空来使用默认的配置

    var myLineChart = $("#myLineChart").lineChart(data, options);

    var data2 = [ {
        value : 150,
        color : "blue", // 使用颜色名称
        label : "蓝队"
    }, {
        value : 250,
        color : "#F7464A", // 自定义颜色
        // highlight: "#FF5A5E", // 自定义高亮颜色
        label : "红队"
    }, {
        value : 50,
        color : 'green',
        label : "绿队"
    }, {
        // 不指定color值，使用随机颜色
        // 
        value : 100,
        color : 'gray',
        label : "随机颜色队"
    } ];
    // 创建饼图
    var myPieChart = $("#myPieChart").pieChart(data2, {
        scaleShowLabels : true
    });
    // 创建环形饼图
    var myDoughnutChart = $("#myDoughnutChart").doughnutChart(data2, {
        segmentShowStroke : false,
        scaleShowLabels : true,
        scaleLabelPlacement : 'outside'
    });
    var data3 = {
        labels : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月" ],
        datasets : [ {
            label : "蓝队",
            color : 'primary',
            data : [ 65, 59, 80, 81, 56, 55, 40 ]
        }, {
            label : "绿队",
            color : 'green',
            data : [ 28, 48, 40, 19, 86, 27, 90 ]
        } ]
    };
    var options = {
        responsive : true
    }; // 图表配置项，可以留空来使用默认的配置
    var myBarChart = $('#myBarChart').barChart(data, options);

}

function afterPageLoad() {
    var data = {
        labels : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
                "十一月", "十二月" ],
        datasets : [ {
            label : "红队",
            color : "red",
            data : [ 65, 59, 80, 81, 56, 55, 40, 44, 55, 70, 30, 40 ]
        }, {
            label : "绿队",
            color : "green",
            data : [ 28, 48, 40, 19, 86, 27, 90, 60, 30, 44, 50, 66 ]
        } ]
    };
    var myLineChart = $("#myLineChart").lineChart(data, {
        bezierCurve : true,
        responsive : true
    });

    var data2 = [ {
        value : 150,
        color : "blue", // 使用颜色名称
        label : "蓝队"
    }, {
        value : 250,
        color : "#F7464A", // 自定义颜色
        // highlight: "#FF5A5E", // 自定义高亮颜色
        label : "红队"
    }, {
        value : 50,
        color : 'green',
        label : "绿队"
    }, {
        // 不指定color值，使用随机颜色
        // 
        value : 100,
        label : "随机颜色队"
    } ];
    // 创建饼图
    var myPieChart = $("#myPieChart").pieChart(data2, {
        scaleShowLabels : true
    });
    // 创建环形饼图
    var myDoughnutChart = $("#myDoughnutChart").doughnutChart(data2, {
        segmentShowStroke : false,
        scaleShowLabels : true,
        scaleLabelPlacement : 'outside'
    });

    // 综合示例
    var myPieChart2 = $("#myPieChart2").pieChart(data2);
    var myDoughnutChart2 = $("#myDoughnutChart2").doughnutChart(data2, {
        segmentShowStroke : false
    });
    $("#myPieChart2")
            .on(
                    'click',
                    function(e) {
                        var point = myPieChart2.getSegmentsAtEvent(e)[0];
                        $('#pieGetSegmentsAtEvent')
                                .html(
                                        point ? '你点击了 <strong><i class="icon text-muted icon-circle"></i> 饼图 </strong>的 <span style="color: #fff; display: inline-block; padding: 0 5px; background-color: '
                                                + point.fillColor
                                                + '">'
                                                + point.label
                                                + '</span>，当前值为 <strong>'
                                                + point.value + '</strong>'
                                                : '你点击了空白地方。');
                    });
    $("#myDoughnutChart2")
            .on(
                    'click',
                    function(e) {
                        var point = myDoughnutChart2.getSegmentsAtEvent(e)[0];
                        $('#pieGetSegmentsAtEvent')
                                .html(
                                        point ? '你点击了 <strong><i class="icon text-muted icon-circle"></i> 环图 </strong>的 <strong style="color: #fff; display: inline-block; padding: 0 5px; background-color: '
                                                + point.fillColor
                                                + '">'
                                                + point.label
                                                + '</strong>，当前值为 <strong>'
                                                + point.value + '</strong>'
                                                : '你点击了空白地方。');
                    });
    $('#updatePie').on(
            'click',
            function() {
                var num = parseInt($('#updatePieNum').val());
                var val = parseInt($('#updatePieValue').val());
                if (!isNaN(num) && !isNaN(val)) {
                    myPieChart2.segments[num].value = val;
                    myPieChart2.update();
                    myDoughnutChart2.segments[num].value = val;
                    myDoughnutChart2.update();
                    var next = Math.max(1, Math.round(Math.random() * 100));
                    var nextIndex = Math.floor(Math.random()
                            * myPieChart2.segments.length);
                    $('#updatePieValue').val(next);
                    $('#updatePieNum').val(nextIndex);
                } else {
                    $.zui.messager.warning('请输入有效的数据。', {
                        placement : 'center'
                    });
                }
            });

    $('#addPie').click(function() {
        var val = parseInt($('#addPieValue').val());
        var label = $('#addPieLabel').val();
        if (!isNaN(val) && label) {
            var dt = {
                value : val,
                label : label
            };
            myPieChart2.addData(dt);
            myDoughnutChart2.addData(dt);

            var next = Math.max(1, Math.round(Math.random() * 100));
            $('#addPieValue').val(next);
            $('#addPieLabel').val('幸运' + next);
        } else {
            $.zui.messager.warning('请输入有效的数据。', {
                placement : 'center'
            });
        }
    });

    $('#removePieData').click(
            function() {
                var idx = parseInt($('#removePieIndex').val());
                if (!isNaN(idx) || !idx) {
                    idx = undefined;
                    myPieChart2.removeData(idx);
                    myDoughnutChart2.removeData(idx);
                    var nextIndex = Math.floor(Math.random()
                            * myPieChart2.segments.length);
                    $('#removePieIndex').val(nextIndex);
                } else {
                    $.zui.messager.warning('请输入有效的数据。', {
                        placement : 'center'
                    });
                }
            });

    var data3 = {
        labels : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月" ],
        datasets : [ {
            label : "蓝队",
            color : 'primary',
            data : [ 65, 59, 80, 81, 56, 55, 40 ]
        }, {
            label : "绿队",
            color : 'green',
            data : [ 28, 48, 40, 19, 86, 27, 90 ]
        } ]
    };
    var myBarChart = $('#myBarChart').barChart(data3, {
        responsive : true
    });
}
