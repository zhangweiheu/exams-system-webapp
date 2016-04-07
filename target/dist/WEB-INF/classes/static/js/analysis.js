/**
 * Created by zhang on 2016/3/11.
 */
/**
 * Created by zhang on 2016/4/7.
 */
$(function () {

    $.ajax({
        method: "GET",
        url: "/api/analysis",
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                var questionType = data.data.questionType;

                $('#container').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {
                        text: '我的技能分布图'
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        type: 'pie',
                        name: '技能占比',
                        data: [
                            ['Java', questionType.Java],
                            ['Spring', questionType.Spring],
                            ['MySQL', questionType.MySQL],
                            ['CSS', questionType.CSS],
                            ['HTML', questionType.HTML],
                            ['Hibernate', questionType.Hibernate],
                            ['操作系统', questionType.操作系统],
                            ['计算机网络', questionType.计算机网络]
                        ]
                    }]
                });

                $('#containerB').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {
                        text: '我的题型分析'
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        type: 'pie',
                        name: '题型占比',
                        data: [
                            ['单选', questionType.单选],
                            ['多选', questionType.多选],
                            ['编程题', questionType.编程题]
                        ]
                    }]
                });


                var right = [
                    questionType.单选Right,
                    questionType.多选Right,
                    questionType.编程题Right,
                    questionType.JavaRight,
                    questionType.SpringRight,
                    questionType.MySQLRight,
                    questionType.CSSRight,
                    questionType.HTMLRight,
                    questionType.HibernateRight,
                    questionType.操作系统Right,
                    questionType.计算机网络Right];
                var wrong = [
                    questionType.单选 - questionType.单选Right,
                    questionType.多选 - questionType.多选Right,
                    questionType.编程题 - questionType.编程题Right,
                    questionType.Java - questionType.JavaRight,
                    questionType.Spring - questionType.SpringRight,
                    questionType.MySQL - questionType.MySQLRight,
                    questionType.CSS - questionType.CSSRight,
                    questionType.HTML - questionType.HTMLRight,
                    questionType.Hibernate - questionType.HibernateRight,
                    questionType.操作系统 - questionType.操作系统Right,
                    questionType.计算机网络 - questionType.计算机网络Right
                ];

                $('#containerC').highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '技能成熟度'
                    },
                    xAxis: {
                        categories: ['单选','多选','编程题','Java', 'Spring', 'MySQL', 'CSS', 'HTML', 'Hibernate', '操作系统', '计算机网络']
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: '正误数'
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        }
                    },
                    legend: {
                        align: 'right',
                        x: -70,
                        verticalAlign: 'top',
                        y: 20,
                        floating: true,
                        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
                        borderColor: '#CCC',
                        borderWidth: 1,
                        shadow: false
                    },
                    tooltip: {
                        formatter: function() {
                            return '<b>'+ this.x +'</b><br/>'+
                                this.series.name +': '+ this.y +'<br/>'+
                                'Total: '+ this.point.stackTotal;
                        }
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                            dataLabels: {
                                enabled: true,
                                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                            }
                        }
                    },
                    series: [{
                        name: '正确',
                        data: right
                    }, {
                        name: '错误',
                        data: wrong
                    }]
                });

                /**得分分析*/

                var userPaper = data.data.userPaper;

                $('#containerD').highcharts({
                    chart: {
                        type: 'spline'
                    },
                    title: {
                        text: '考试历程'
                    },
                    subtitle: {
                        text: '技能提升之路'
                    },
                    xAxis: {
                        type: 'datetime',
                        dateTimeLabelFormats: { // don't display the dummy year
                            month: '%e. %b',
                            year: '%b'
                        }
                    },
                    yAxis: {
                        title: {
                            text: '得分'
                        },
                        min: 0
                    },
                    tooltip: {
                        formatter: function() {
                            return '<b>'+ this.series.name +'</b><br/>'+
                                Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m';
                        }
                    },

                    series: [{
                        name: 'paper',
                        // Define the data points. All series have a dummy year
                        // of 1970/71 in order to be compared on the same x axis. Note
                        // that in JavaScript, months start at 0 for January, 1 for February etc.
                        data: userPaper
                    }]
                });

            } else {
                layer.alert("获取数据出错", {icon: 11, offset: '150px'});
            }
        }
    });

});
