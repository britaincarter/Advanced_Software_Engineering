<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<t:page>
    <jsp:attribute name="title">Welcome!</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" id="welcome">
			<h2 class="board-title">${!empty title ? title : 'All Messages' }</h2>
			<c:if test="${canEdit}">
			<span class="board-settings">
				<a href="${board}/settings">Settings</a>
			</span>
			</c:if>
				<div class="row fill">
					<div class="col-md-2">
						<h3>Boards</h3>
						Boards go here
					</div>
					<div class="col-md-10 fill">
						<div class="row">
							<h3>Messages</h3>
						</div>
						<div class="row fill">
							<div class="col-sm-7 fill">
								<div class="msgarea fill">
									<c:forEach var="msg" items="${allMessages}">
					            	<div id="message_${msg.id}" class="message">
					                	<a href="/m/${board}/${msg.id}"><c:out value="${msg.text}" /></a>
					            	</div>
					        		</c:forEach>
								</div>
							</div>
							<div class="col-sm-5">
								<div class="row">
									<!-- <textarea id="msgTxt">Your message</textarea> -->
									<div id="msgTxt" contenteditable="true">Your message</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<button type="button" id="btnSend" class="btn btn-primary btn-right">
											Send
										</button>
									</div>
								</div>
								<div class="row">
									<label for="txtStyleName">Style name:</label>
									<input id="txtStyleName" type="text" />
								</div>
								<div class="row">Attributes:</div>
								<div class="row">
									<div class="col-sm-6">
										<select id="selUserAttr">
										<c:forEach var="attrib" items="${attribs}">
										<option value="${attrib.getId()}">${attrib.getName()}</option>
										</c:forEach>
										</select>
									</div>
									<div class="col-sm-6">
										<input type="text" id="txtVal" />
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<button id="btnAddStyle" type="button" class="btn btn-info btn-right">Add Style</button>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<select id="selAttr" size="6" class="attribsel"></select>
									</div>
									<div class="col-sm-6">
										<select id="selVal" size="6" class="attribsel"></select>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<button id="btnRemStyle" type="button" class="btn btn-danger btn-right">Remove attribute</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
			
			<!-- 
    		<ul>
        		<c:forEach var="msg" items="${allMessages}">
            	<li>
                	<a href="/m/${board}/${msg.id}"><c:out value="${msg.text}" /></a>
            	</li>
        		</c:forEach>
    		</ul>

    		<a href="/m_form/${board}">Post Message</a>
    		 -->
            
        </div>
        
        <script type="text/javascript">
        	var boardId = ${board};
        </script>
        <script src="/resources/js/m_list.js"></script>
        
    </jsp:body>
</t:page>
