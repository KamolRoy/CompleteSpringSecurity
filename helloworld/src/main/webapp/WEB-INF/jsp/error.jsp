
<%@ include file="/WEB-INF/include/header.jsp"%>

<div class="container">

	

	<div class="alert alert-danger">
		<p> <span class="glyphicon glyphicon-exclamation-sign" style="font-size: 1.2em;"></span>
			 There was an unexpected error (type=${error }, status=${status}): <i>
				${message }</i>
		</p>
		<p>
			Click <a href="/">here</a> to return home page
		</p>
	</div>
</div>

<%@ include file="/WEB-INF/include/footer.jsp"%>