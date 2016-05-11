/**
 * Created by zhang on 2016/4/7.
 */
$(function () {
    $.ajax({
        method: "GET",
        url: "/api/analysis/system",
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                var questionType = data.data.question;

                $('#container').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {
                        text: '系统知识技能数量分布占比'
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
                        name: '占比',
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
                        text: '系统题型数量分析'
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
                    questionType.单选Done - questionType.单选Right,
                    questionType.多选Done - questionType.多选Right,
                    questionType.编程题Done - questionType.编程题Right,
                    questionType.JavaDone - questionType.JavaRight,
                    questionType.SpringDone - questionType.SpringRight,
                    questionType.MySQLDone - questionType.MySQLRight,
                    questionType.CSSDone - questionType.CSSRight,
                    questionType.HTMLDone - questionType.HTMLRight,
                    questionType.HibernateDone - questionType.HibernateRight,
                    questionType.操作系统Done - questionType.操作系统Right,
                    questionType.计算机网络Done - questionType.计算机网络Right
                ];

                $('#containerC').highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '系统知识技能作答分析'
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
            } else {
                layer.alert("获取数据出错", {icon: 11, offset: '150px'});
            }
        }
    });

});
