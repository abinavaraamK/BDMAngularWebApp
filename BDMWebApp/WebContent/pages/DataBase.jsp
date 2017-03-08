<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
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
<script>

function test(configurationName){
var selectBox = document.getElementById("dbdetails");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    alert(selectedValue);
	
}

function submitForm()
{
    document.registerForm.submit();
}

function changeFormValueAndSubmit(buttonName) {
	   var formData = $('form').serialize();
	   jQuery.post('/path', formData, function(d) {
	   
	   });

	   
	}


$(document).ready(function() {
	 $("#configurationName").on("change", function() {
			
			
						
			var configurationName = $('#configurationName').val();	
			var data = 'configurationName='
					+ encodeURIComponent(configurationName);
			alert(data)
			$.ajax({
				url : "/path",
				data : data,
				type : "GET",
			
			success : function(response) {
					$('#driverName').val(response.driverName); 
					$('#url').val(response.url); 
					$('#portNo').val(response.portNo); 
					$('#schemaName').val(response.schemaName); 
					$('#userName').val(response.userName); 
					$('#password').val(response.password); 
				},
				error : function(xhr, status, error) {
					alert(xhr.responseText);
				}
			});
			return false;
		});
	});

</script>
</head>
<body class="row" style="background: skyblue;">
	<div class="col-md-offset-4 col-md-5" id="centerDiv">
		<h3 class="col-md-offset-2 col-md-10">Data Base Configuration</h3>
		<form:form action="/BaseDataManagerWebApp/tableDetails"  commandName="dbDetails" >

			<div class="col-md-10 margin" id="configuration">
				<p class="col-md-6">select the configuration</p>

				<form:select path="configurationName" id="configurationName">
					<form:option value="0">Select </form:option>
					<form:options items="${ListConfigDetails}" />
				</form:select>

			</div>


			<div id="DriverName" class="col-md-10 margin">
				<span class="col-md-5">Driver Name:</span>
				<form:input id="driverName" path="driverName" />
			</div>
			<div id="URL" class="col-md-10 margin">
				<span class="col-md-5">URL:</span>
				<form:input id="url" path="url" class="col-md-6" />
			</div>
			<div id="PortNo" class="col-md-10 margin">
				<span class="col-md-5">Port No:</span>
				<form:input id="portNo" path="portNo" class="col-md-6" />
			</div>
			<div id="SchemaName" class="col-md-10 margin">
				<span class="col-md-5">Schema Name:</span>
				<form:input id="schemaName" path="schemaName" class="col-md-6" />
			</div>
			<div id="UserName" class="col-md-10 margin">
				<span class="col-md-5">User Name:</span>
				<form:input id="userName" path="userName" class="col-md-6" />
			</div>
			<div id="Password" class="col-md-10 margin">
				<span class="col-md-5">Password:</span>
				<form:input id="password" path="password" class="col-md-6" />
			</div>



			<div id="buttons button-sm"
				class="col-md-offset-1 col-md-10 margin margin">
				<button type="submit" class="col-md-offset-1 col-md-4">Next</button>
				<button type="button" class="col-md-offset-1 col-md-4">Test</button>
			</div>




		</form:form>
	</div>
</body>
</html>