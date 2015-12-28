<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
<div id="stylized" class="myform">
	<form:form action="save.do"	method="post" enctype="multipart/form-data" commandName="group">
		<h1>Group</h1>
		<label>Name <span class="small">Group name</span></label>
		<form:input path="name" />
		<label>Photo <span class="small">Upload group photo</span></label>
		<input type="file" name="pic" />
		<label>Description <span class="small">Group description</span></label>
		<form:textarea path="description" />
		<button type="submit">Submit</button>
		<div class="spacer"></div>
	</form:form>
</div>
</body>
</html>