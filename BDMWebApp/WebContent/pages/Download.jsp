<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Result Page</title>
<spring:url value="resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="resources/css/bdm_appp.css" var="coreCss" />
<spring:url value="resources/js/jquery-2.1.0.min.js" var="Jquery"/>
<spring:url value="resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="resources/js/bdm_app.js" var="bdmApp" />
<link rel="stylesheet" href="${bootstrapCss}">
<link rel="stylesheet" href="${coreCss}">
<script src="${Jquery}" type="text/javascript"></script>
<script src="${bootstrapJs}" type="text/javascript"></script>
<script src="${bdmApp}"></script>

<script src="js/bdm_app.js"></script>
</head>
<form:form method="get" >
<c:set var="contextPath" value="${fileName}"/>
<body class="row" >
	<div class="col-md-12" style="margin-top: 10%;">
		<span class="col-md-offset-4 col-md-3" style="color: aliceblue;">BUILD STATUS</span>
		<span class="col-md-5" style="color: aliceblue;">BUILD STATUS</span>
		<span class="col-md-offset-2 col-md-5" style="color: aliceblue;padding-left: 20%;">Download Generated War File</span>
		 <a class="col-md-5" href="<c:url value='/downloadWar' />">Download This File (located outside project, on file system)</a>
	</div>
</body>
</form:form>
</html>