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
	<form:form action="save.do"	method="post" id="messageForm">
		<h1>Message</h1>
		<label>From <span class="small">send message from</span></label>
		<select name="from">
			<c:forEach var="person" items="${people}">
				<option value="${person.id}">${person.name}</option>
			</c:forEach>
		</select>
		<label>To <span class="small">send message to</span></label>
		<select name="to">
			<c:forEach var="person" items="${people}">
				<option value="${person.id}">${person.name}</option>
			</c:forEach>
		</select>
		<label>Message <span class="small">Add your message</span></label>
		<textarea name="content" rows="6"></textarea>
		<button type="submit">Submit</button>
		<div class="spacer"></div>
	</form:form>
</div>
</body>
</html>