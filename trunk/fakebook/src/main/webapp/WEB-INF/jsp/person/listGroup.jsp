<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/style.css"/>" />
</head>
<body>
<div style="float: left">
	<img src="<c:url value="/person/photo.do"/>?id=${person.id}" />
	<h2><c:out value="${person.name}" /></h2>
	<h2><c:out value="${person.gender}" /></h2>
</div>
<div align="center">
<table id="list">
	<tbody>
		<tr style="background-color: #3b5998">
			<td colspan="3">
				<a href="<c:url value="/message/list.do"/>?id=${person.id}" rel="country2" class="link">Wall</a>
				<a href="<c:url value="/person/list.do"/>?id=${person.id}" rel="country4" class="link">Friends</a>
				<a href="<c:url value="/person/listGroup.do"/>?personId=${person.id}" class="link">Group</a>
			</td>
		</tr>
		<c:forEach var="group" items="${person.groups}">
			<tr>
				<td width="20%" rowspan="3">
					<a href="<c:url value="/group/list.do"/>?id=${group.id}">
						<img class="photo" src="<c:url value="/group/photo.do"/>?id=${group.id}" />
					</a>
				</td>
				<td align="left" valign="top" nowrap="nowrap">
					<div style="font-size:11pt;font-weight:bold;color:#3b59ba">
						<c:out value="${group.name}" />
					</div>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" nowrap="nowrap" style="border-top-color: #fff">
					<div>
						Size : <c:out value="${fn:length(group.members)}" /> members
					</div>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" style="border-top-color: #fff">
					<div style="color: #333333">
						<c:out value="${group.description}" />
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>