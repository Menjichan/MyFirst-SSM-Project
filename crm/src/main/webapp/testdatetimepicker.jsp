<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <%--引入jQuery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--引入bootstrap框架--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" />
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--引入bootstrap日历插件--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" />
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>测试bs日历插件</title>
<script>
    $(function () {
        //调用插件工具函数
        $("#myDate").datetimepicker({
            language:'zh-CN',//语言种类
            format:'yyyy-mm-dd',//日期格式
            minView:'month',//可以选择的最小视图
            initialDate:new Date(),//初始化为今天时间
            autoclose:true,//选择完即关闭
            todayBtn:true,//是否显示"今天"的按钮
            clearBtn:true,//是否显示"清空"按钮，因为插件中该按键使用的是英文，可以自己进去修改成中文
        });
    });
</script>
</head>
<body>
    <input type="text" id="myDate" readonly>
</body>
</html>
