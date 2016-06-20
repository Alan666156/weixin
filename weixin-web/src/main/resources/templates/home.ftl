<!DOCTYPE html>
<html>
<#assign ctx=request.contextPath />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="webkit">
<title>运营数据大报表</title>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css" />
<script src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="/js/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
<script src="/js/public.js"></script>
<style type="text/css">
	.endDate{margin-top: 4px;}
	.date-clear{
	  	cursor: pointer;
	  	position:absolute;
	  	top:34px;
	  	left:170px;
	  }
</style>

</head>
<body class="index">
<!--<div class="form-group pull-left col-sm-2" style="z-index: 0;margin-top:30px;">
		<input type="text" value="开始日期" readonly class="form_datetime form-control startDate" name="startTime">
		<a class="icon-remove date-clear" id="beginDate" href="#"></a>
</div>
<div class="form-group pull-left col-sm-2" style="z-index: 0;margin-top:30px;">
		<input type="text" value="结束日期" readonly class="form_datetime form-control endDate" name="endTime">
		<a class="icon-remove date-clear" id="endDate" href="#"></a>
</div>-->  
<div class="submitBtns pull-right" style="margin-top:30px;">
	
	<div class="form-group pull-left col-sm-2" style="z-index: 0;">
	    <input type="text" class="form-control beginDate input-date" placeholder="开始日期" name="beginDate" readonly>
	    <a class="icon-remove date-clear" id="beginDate" href="#"></a>
	</div>
	<div class="form-group pull-left col-sm-2">
	    <input type="text" class="form-control endDate input-date" placeholder="结束日期" name="endDate" readonly>
	    <a class="icon-remove date-clear" id="endDate" href="#"></a>
	</div>
	
	  
	<button type="button" class="btn btn-primary" onclick="exportExcel('customer')">客户数据导出</button>
    <button type="button" class="btn btn-primary" onclick="exportExcel('getAuditSupplierInfo')">供应商数据导出</button>
    <button type="button" class="btn btn-primary" onclick="exportExcel('service')">服务数据导出</button>
	<button type="button" class="btn btn-primary" onclick="exportExcel('getAuditOrderInfo')">订单数据导出</button>
	<button type="button" class="btn btn-primary" onclick="exportExcel('ser')">流量数据导出</button>


    
    <button type="button" class="btn btn-primary" onclick="changeView('day')">日</button>
    <button type="button" class="btn btn-primary" onclick="changeView('week')">周</button>
    <button type="button" class="btn btn-primary" onclick="changeView('month')">月</button>
	<!--
	<a href='/orderDetail?id=<@encrypt val="123"/>' />加密参数1111111111111111111111</a>
	-->
  </div>
<div id="container" style="min-width:800px;height:400px"></div>
	
</div>

<script type="text/javascript">
	 /**$(".form_datetime").datetimepicker({
			language:  'zh-CN',
		    format: 'yyyy-mm-dd',
		    autoclose:true,
		    todayHighlight: 1,
			minView: 2,
			endDate:new Date()
	});*/
		
	//导出用户数据	
	function exportExcel(path){
		var startTime = "<@encrypt val="2016-05-01"/>";
		var endTime = "<@encrypt val="2016-05-10"/>";
		window.open("/exportReportExcel?startTime="+startTime+"&endTime=" + endTime);
	}
	
	function changeView(name){
		
	}
	
	$(function () {
	    $('#container').highcharts({
	        title: {
	            text: '旅游圈运营大数据',
	            x: -20 //center
	        },
	        subtitle: {
	            text: 'www.lvyouquan.cn',
	            x: -20
	        },
	        xAxis: {
	            categories: ['1月', '2月', '3月', '4月', '5月', '6月','7月', '8月', '9月', '10月', '11月', '12月']
	        },
	        yAxis: {
	            title: {
	                text: '大数据报表分析'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: [{
	            name: '上海站',
	            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
	        }, {
	            name: '苏北站',
	            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
	        }, {
	            name: '无锡站',
	            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '苏州站',
	            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
	        }, {
	            name: '无锡站',
	            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '杭州站',
	            data: [10, 20, 12, 24, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '宁波站',
	            data: [1.0, 0.2, 7, 10, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '温州站',
	            data: [-0.3, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '金华站',
	            data: [-0.4, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '南京站',
	            data: [-0.6, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
	        }, {
	            name: '合肥站',
	            data: [2.0, -0.7, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 2.0]
	        }, {
	            name: '北京站',
	            data: [10, -0.6, 2.5, 8.4, 13.5, 17.0, 13.6, 18.9, 14.3, 9.0, 3.9, 3.0]
	        }, {
	            name: '天津站',
	            data: [3.6, 15, 3.5, 8.4, 13.5, 17.0, 11.6, 15.9, 12.3, 9.0, 3.9, 11.0]
	        }, {
	            name: '广东站',
	            data: [1.5, 2, 3.5, 8.4, 13.5, 17.0, 12.6, 17.9, 14.7, 9.0, 3.9, 8.0]
	        }, {
	            name: '河北站',
	            data: [-0.5, 0.7, 3.5, 8.4, 13.5, 17.0, 19.2, 17.9, 11.3, 8.0, 3.9, 7.0]
	        }, {
	            name: '山东站',
	            data: [-0.9, 1.5, 1.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 1.0, 4.5, 6.0]
	        }, {
	            name: '四川站',
	            data: [5, 3.3, 5.7, 8.8, 11.5, 19.0, 18.6, 17.9, 14.3, 9.0, 3.9, 2.0]
	        }]
	    });
	});
</script>
</body>
</html>
