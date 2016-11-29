<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
	<h1>Single Message Viewer</h1>

    <p>${messageText}</p>

    <a href="/m_list/${board}">View all messages</a>
            
        </div>
    </jsp:body>
</t:page>
