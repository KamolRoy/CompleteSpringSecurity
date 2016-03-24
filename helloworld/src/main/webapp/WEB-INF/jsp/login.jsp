
<%@ include file="/WEB-INF/include/header.jsp"%>

<div class="container">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">Please sing in</h3>
		</div>
		<!-- end of panel heading -->

		<div class="panel-body">
			<c:if test="${param.error != null }">
				<div class="alert alert-danger">Invalid username and password.
				</div>
			</c:if>

			<c:if test="${param.logout != null }">
				<div class="alert alert-danger">You have been logged out.</div>
			</c:if>

			<form:form role="form"	method="post">
				<div class="form-group">
					<label for="username">Email address</label>
					<input type="email" id="username" name="username" class="form-control" placeholder="Enter email" />
					<p class="help-block">Enter your email address</p>
				</div>
				<div class="from-group">
					<label for="password">Password</label>
					<input type="password" id="password" name="password" class="form-control" placeholder="Password"/>
					<form:errors cssClass="error" path="password"/>
				</div>
				
				<div class="form-group">
                <div class="checkbox">
                    <label>
                        <input name="remember_me" type="checkbox"> Remember me
                    </label>
                </div>
            </div>
			
			<button type="submit" class="btn btn-primary">Sign In</button>
			<a class="btn btn-default" href="/forgot-password">Forgot Password</a>
			</form:form>


		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel-primary -->
</div>
<!-- end of container  -->

<%@ include file="/WEB-INF/include/footer.jsp"%>