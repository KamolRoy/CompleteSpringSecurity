
<%@ include file="/WEB-INF/include/header.jsp"%>

<div class="container">

	<div class="panel panel-primary">

		<div class="panel-heading">
			<h3 class="panel-title">Forgot password?</h3>
		</div>

		<div class="panel-body">

			<form:form modelAttribute="forgotPasswordForm" role="form">

				<form:errors />

				<div class="form-group">
					<form:label path="email">Email address</form:label>
					<form:input path="email" type="email" class="form-control"
						placeholder="Enter email" />
					<form:errors cssClass="error" path="email" />
					<p class="help-block">Please enter your email id</p>
				</div>

				<button type="submit" class="btn btn-default">Reset
					password</button>

			</form:form>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/include/footer.jsp"%>