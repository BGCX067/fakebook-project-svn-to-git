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
	<form:form action="save.do"	method="post" enctype="multipart/form-data" commandName="person">
		<h1>Profile</h1>
		<label>Name <span class="small">Add your name</span></label>
		<form:input path="name" />
		<label>Gender <span class="small">Choice your gender</span></label>
		<form:select path="gender" items="${gender}" />
		<label>Photo <span class="small">Upload your photo</span></label>
		<input type="file" name="pic" />
		<button type="submit">Submit</button>
		<div class="spacer"></div>
	</form:form>
</div>
</body>
</html>