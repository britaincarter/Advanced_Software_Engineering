<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>

	<jsp:attribute name="title">Boards</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="boards">
	<h1>Boards</h1>

    <ul>
   		<c:forEach var="board" items="${boards}">
       	<li>
           	<a href="/m_list/${board.id}"><c:out value="${board.name}" /></a>
       	</li>
   		</c:forEach>
	</ul>
            
        </div>
    </jsp:body>

</t:page>