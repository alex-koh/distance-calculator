<%@ page import="java.util.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.magenta.calculator.AdminAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<%
    Properties prop = new Properties();
    prop.load(AdminAction.class.getResourceAsStream("test.state.properties"));
    if (prop.getProperty("date")==null) {
        prop.setProperty("date", new Date().toString());
        String file = AdminAction.class.getResource("test.state.properties").getFile();
        prop.store(new PrintWriter(file),"");
    }
    String date = prop.getProperty("date");
%>
<h1> Hello Kitty Version <%=date%> </h1>
<a href="<%=request.getContextPath()%>/Main.action"><h1>GOGOGO</h1></a>

<p><h2>application</h2>
    <pre>
        <s:property value="#application['com.magenta.calculator.cities']"/>
        <s:property value="#application['com.magenta.calculator.cities'].size"/>
        <s:property value="#application['com.magenta.calculator.calcFactory']"/>
        <s:property value="#application['com.magenta.calculator.dataSource']"/>
    </pre>
<h2>session</h2>
    <pre>
        <s:property value="#session.toString().replace(' ','\n')" />
    </pre>
<h2>request</h2>
    <pre>
        <s:property value="#request.toString().replace(' ','\n')" />
    </pre>

<s:actionerror/>
<s:actionmessage/>
</body>
</html>