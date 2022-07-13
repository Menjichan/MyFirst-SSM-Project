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
    <title>测试文件下载</title>
<script type="text/javascript">
    $(function () {
       $("#fileDownloadBtn").click(function () {
          window.location.href="workbench/activity/fileDownload.do";
       });
    });
</script>
</head>
<body>
    <input type="button" value="下载" id="fileDownloadBtn">
</body>
</html>
