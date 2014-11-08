<%@ page import="java.util.Date" %>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1> Hello Kitty Version <%=
    new Date(
        new File(getClass().getResource(getClass().getSimpleName()+".class")
                .getFile()).lastModified()
    ).toString()
%> </h1>
<a href="<%=request.getContextPath()%>/Main.action"><h1>GOGOGO</h1></a>
<a href="<%=request.getContextPath()%>/Admin.action"><h1>Admin</h1></a>
</body>
</html>