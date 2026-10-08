<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/common/header.jspf" %>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">👥 Groups &amp; Meetups</h3>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/groups/create">+ Create group</a>
  </div>

  <c:if test="${not empty param.msg}"><div class="alert alert-info"><c:out value="${param.msg}"/></div></c:if>
  <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>

  <div class="row g-3">
    <c:forEach items="${groups}" var="g">
      <div class="col-md-6 col-lg-4">
        <div class="card shadow-sm h-100">
          <div class="card-body d-flex flex-column">
            <div class="d-flex justify-content-between">
              <h5 class="card-title"><c:out value="${g.name}"/></h5>
              <span class="badge bg-secondary align-self-start"><c:out value="${g.category}"/></span>
            </div>
            <p class="card-text text-muted small"><c:out value="${g.description}"/></p>
            <p class="small mb-3">By <strong><c:out value="${g.ownerName}"/></strong> · ${g.memberCount}/${g.maxMembers} members</p>
            <div class="mt-auto">
              <c:choose>
                <c:when test="${g.myRole == 'OWNER'}"><span class="badge bg-success">You own this group</span></c:when>
                <c:when test="${g.myRole == 'MEMBER'}">
                  <form method="post" action="${pageContext.request.contextPath}/groups/leave">
                    <input type="hidden" name="groupId" value="${g.id}">
                    <button class="btn btn-outline-danger btn-sm">Leave</button>
                  </form>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${pageContext.request.contextPath}/groups/join">
                    <input type="hidden" name="groupId" value="${g.id}">
                    <button class="btn btn-success btn-sm">JOIN</button>
                  </form>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
    <c:if test="${empty groups}"><p class="text-muted">No groups yet. Create the first one!</p></c:if>
  </div>
</div>
<%@ include file="/WEB-INF/common/footer.jspf" %>