<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Data Base Configuration</title>

<spring:url value="resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="resources/css/bdm_app.css" var="coreCss" />
<spring:url value="resources/js/jquery-2.1.0.min.js" var="Jquery"/>
<spring:url value="resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="resources/js/bdm_app.js" var="bdmApp" />


<link rel="stylesheet" href="${bootstrapCss}">
<link rel="stylesheet" href="${coreCss}">
<script src="${Jquery}" type="text/javascript"></script>
<script src="${bootstrapJs}" type="text/javascript"></script>
<script src="${bdmApp}"></script>
</head>
<body class="row" style="background: skyblue;">
	<div class="col-md-offset-4 col-md-5" id="centerDiv">
		<h3 class="col-md-offset-2 col-md-10">Data Base Configuration</h3>
		<form action="/test" name="DBform" method="get" >
			<div class="col-md-10 margin" id="configuration">
				<p class="col-md-6">select the configuration</p>
				<select class="col-md-5">
				  <option value=" ">Options</option>
				  <option value="HEXTRM3 - Oracle">HEXTRM3 - Oracle</option>
				  <option value="Scott - Oracle">Scott - Oracle</option>
				  <option value="Hex-Static">Hex-Static</option>
				</select>
	  		</div>
			<div id="Driver Name" class="col-md-10 margin">
				<span class="col-md-5">Driver Name:</span>
				<input type="text" class="col-md-6"/>
			</div>
			<div id="URL" class="col-md-10 margin">
					<span class="col-md-5">URL:</span>
					<input type="text" class="col-md-6"/>
			</div>
			<div id="Port No" class="col-md-10 margin">
				<span class="col-md-5">Port No:</span>
				<input type="text" class="col-md-6"/>
			</div>
			<div id="Schema Name" class="col-md-10 margin">
				<span class="col-md-5">Schema Name:</span>
				<input type="text" class="col-md-6"/>
			</div>
			<div id="User Name" class="col-md-10 margin">
				<span class="col-md-5">User Name:</span>
				<input type="text" class="col-md-6"/>
			</div>
			<div id="Password" class="col-md-10 margin">
				<span class="col-md-5">Password:</span>
				<input type="text" class="col-md-6"/>
			</div>
			<div id="buttons button-sm" class="col-md-offset-1 col-md-10 margin margin">
				<button type="submit" class="col-md-offset-1 col-md-4">Next</button>
				<button type="button" class="col-md-offset-1 col-md-4">Test</button>
			</div>
		</form>
	</div>
</body>
</html>