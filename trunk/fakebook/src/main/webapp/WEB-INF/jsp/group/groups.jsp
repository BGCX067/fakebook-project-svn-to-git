<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
	<link rel="stylesheet" type="text/css"
		href="<c:url value="/css/style.css"/>" />
	<script language="javascript" src="<c:url value="/js/jquery-1.3.2.js"/>"></script>
	<script language="javascript" src="<c:url value="/js/jquery.form.js"/>"></script>
	<script language="javascript" src="<c:url value="/js/jquery.selectboxes.js"/>"></script>
	<script language="javascript">
	  $(document).ready(function(){
	    $("#loading").bind("ajaxSend", function(){
	       $(this).show();
	       $('#message').html("");
	    }).bind("ajaxComplete", function(){
	       $(this).hide();
	    });
	    $('#group').submit(function() { 
	        $(this).ajaxSubmit({ 
	        	target:'#message'
	        }); 
	        return false; 
	    }); 
	  });
	  
	  function getFriends(sel) {
		$.ajax({
			type: "GET",
			url: "getMembers.do",
			data: "id=" + sel.value,
			success: function(response){
				var friends = eval("(" + response + ")");
				$('#memberIds').children().each(function(){
					this.selected = false;
				});
				$('#memberIds').selectOptions(friends);
			}
		 });
	  }
	  
	</script>
</head>
<body>
<div id="loading">Loading....</div>
<div id="stylized" class="myform" style="width:800px">
	<form:form id="group" action="saveMembers.do" method="post">
		<h1>Group</h1>
		<div id="message"></div>
		<label>Group<span class="small">choice group</span></label>
		<select name="groupId" size="10" onchange="getFriends(this);">
			<c:forEach var="group" items="${allGroups}">
				<option value="${group.id}">${group.name}</option>
			</c:forEach>
		</select>
		<label>Member<span class="small">choice members</span></label>
		<select id="memberIds" name="memberIds" multiple="multiple" size="10">
			<c:forEach var="person" items="${allPeople}">
				<option value="${person.id}">${person.name}</option>
			</c:forEach>
		</select>
		<button type="submit">Save</button>
		<div class="spacer"></div>
	</form:form>
</div>
</body>
</html>