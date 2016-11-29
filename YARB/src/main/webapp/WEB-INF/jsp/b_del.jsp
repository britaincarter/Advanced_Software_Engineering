<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
 
            <h2>Delete board <i>${boardName}?</i></h2>
            <br/>
            <p>Are you sure?</p>
            <form method="POST">
            	<button type="submit" name="del" value="yes" class="bigbutton btn btn-lg btn-success">Yes</button>
            	<a href="${nolink}"><button type="button" class="bigbutton btn btn-lg btn-primary">No</button></a>
            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
    </jsp:body>
</t:page>