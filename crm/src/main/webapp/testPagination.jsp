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
    <%--引入pagination插件--%>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" />
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <title>演示bs分页插件</title>
    <script type="text/javascript">
        $(function () {
           $("#demo01_div").bs_pagination({
               currentPage:1,//当前页数
               rowsPerPage:10,//每页显示多少条记录
               totalPages:100,//总页数，该插件必填参数
               totalRows:1000,//总记录数

               visiblePageLinks:7,//页码卡片最多显示多少个

               showGoToPage:false,//是否显示输入页码跳转页面，默认是true
               showRowsPerPage:false,//是否显示每页条数，默认是true
               showRowsInfo:false,//是否显示记录信息，默认是true

               onChangePage:function (event,pageObj) {
                   alert(pageObj.currentPage);
                   alert(pageObj.rowsPerPage);
               }
           });
        });
    </script>
</head>
<body>
    <div id="demo01_div"></div>
</body>
</html>
