<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>测试文件上传</title>
</head>
<body>
    <form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="myFile"><br>
        <input type="text" name="myName"><br>
        <input type="submit" value="上传">
    </form>
</body>
</html>
