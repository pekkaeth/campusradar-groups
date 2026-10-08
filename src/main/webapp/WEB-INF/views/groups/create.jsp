<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/common/header.jspf" %>
<div class="container mt-4" style="max-width:560px;">
  <h3>Create a group</h3>
  <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/groups/create" class="card card-body shadow-sm">
    <div class="mb-3"><label class="form-label">Group name</label>
      <input type="text" name="name" class="form-control" minlength="3" maxlength="100" required></div>
    <div class="mb-3"><label class="form-label">Category</label>
      <select name="category" class="form-select">
        <option>Study</option><option>Sports</option><option>Music</option>
        <option>Coding</option><option>Gaming</option><option>Other</option>
      </select></div>
    <div class="mb-3"><label class="form-label">Description</label>
      <textarea name="description" class="form-control" rows="3" maxlength="500"></textarea></div>
    <button class="btn btn-primary">Create</button>
    <a class="btn btn-link" href="${pageContext.request.contextPath}/groups/list">Cancel</a>
  </form>
</div>
<%@ include file="/WEB-INF/common/footer.jspf" %>