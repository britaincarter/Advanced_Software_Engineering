<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
 
            <h1>Post Message</h1>

    		<form action="/b/${board}/add" method="POST">
        		Message: <input type="text" name="message">
        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        		
        		<br><input type="submit" value="Submit" />
        		
   			</form>
            
        </div>
    </jsp:body>
</t:page>
