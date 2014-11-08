<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Upload new cities</title>
</head>
<body>
    <s:form action="Upload" method="post" enctype="multipart/form-data">
        <s:file name="mapOfCities" label="Cities"/>
        <s:submit/>
    </s:form>
</body>
</html>