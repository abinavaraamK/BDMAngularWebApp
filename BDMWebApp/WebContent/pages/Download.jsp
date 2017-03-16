<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Result Page</title>
<spring:url value="resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="resources/css/bdm_app.css" var="coreCss" />
<spring:url value="resources/js/jquery-2.1.0.min.js" var="Jquery"/>
<spring:url value="resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="resources/js/bdm_app.js" var="bdmApp" />
<link rel="stylesheet" href="${bootstrapCss}">
<link rel="stylesheet" href="${coreCss}">
<script src="${Jquery}" type="text/javascript"></script>
<script src="${bootstrapJs}" type="text/javascript"></script>
<script src="${bdmApp}" type="text/javascript"></script>
</head>
<form:form method="get" >
<!--<c:set var="contextPath" value="${fileName}"/>-->
<body class="row" >
	<div class="col-md-12" style="margin-top: 10%;">
		<span class="col-md-3" id="status">BUILD STATUS</span>
		<span class="col-md-5" id="spanWar">Generated War File</span>
		<a class="col-md-5" href="<c:url value='/downloadWar' />" id="downloadWar">Download War File </a>
	</div>
</body>
</form:form>
</html>