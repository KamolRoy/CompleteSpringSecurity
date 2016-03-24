<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ include file="/WEB-INF/include/header.jsp"%>

<div class="container">


	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Sign-up Form</h3>
		</div>
		<div class="panel-body">
		
		<form:form modelAttribute="signupForm" role="form">
			<form:errors/>
			<div class="form-group">
				<form:label path="email">Email address</form:label>
				<form:input path="email" type="email" class="form-control" placeholder="Enter email" />
				<form:errors cssClass="error" path="email"></form:errors>
				<p class="help-block">Enter a unique email address. It will also be your login id.</p>
			</div>
			
			<div class="form-group">
				<form:label path="name">Name</form:label>
				<form:input path="name" class="form-control" placeholder="Enter name" />
				<form:errors cssClass="error" path="name"></form:errors>
				<p class="help-block">Enter your display name.</p>
			</div>
			
			<div class="form-group">
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Password" />
				<form:errors cssClass="error" path="password"></form:errors>
			</div>
			
			<button type="submit" class="btn btn-success">Submit</button>
			
		</form:form>
		
		</div>
	</div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp"%>