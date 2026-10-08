<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/common/header.jspf" %>
<div class="container mt-4">
  <a href="${pageContext.request.contextPath}/groups/list">&larr; All groups</a>
  <div class="row mt-2 g-4">
    <div class="col-lg-8">
      <h3><c:out value="${group.name}"/> <span class="badge bg-secondary fs-6"><c:out value="${group.category}"/></span></h3>
      <p class="text-muted"><c:out value="${group.description}"/></p>

      <div class="card shadow-sm">
        <div class="card-header">💬 Message board</div>
        <div class="card-body" style="max-height:420px; overflow-y:auto;">
          <c:forEach items="${messages}" var="m">
            <div class="mb-3">
              <strong><c:out value="${m.userName}"/></strong>
              <small class="text-muted ms-2"><fmt:formatDate value="${m.postedAt}" pattern="dd MMM, HH:mm" timeZone="Asia/Kolkata"/></small>
              <div><c:out value="${m.text}"/></div>
            </div>
          </c:forEach>
          <c:if test="${empty messages}"><p class="text-muted mb-0">No messages yet. Say hello!</p></c:if>
        </div>
        <div class="card-footer">
          <form method="post" action="${pageContext.request.contextPath}/groups/post" class="d-flex gap-2">
            <input type="hidden" name="groupId" value="${group.id}">
            <input type="text" name="message" class="form-control" maxlength="500" placeholder="Write a message..." required>
            <button class="btn btn-primary">Send</button>
          </form>
        </div>
      </div>
    </div>

    <div class="col-lg-4">
      <div class="card shadow-sm">
        <div class="card-header">Members (${group.memberCount}/${group.maxMembers})</div>
        <ul class="list-group list-group-flush">
          <c:forEach items="${members}" var="n"><li class="list-group-item"><c:out value="${n}"/></li></c:forEach>
        </ul>
      </div>
      <p class="small text-muted mt-2">Owner: <c:out value="${group.ownerName}"/></p>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/common/footer.jspf" %>