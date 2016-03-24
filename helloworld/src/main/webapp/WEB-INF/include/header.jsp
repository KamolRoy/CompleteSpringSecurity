<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring 4</title>
<link rel="stylesheet"
	href="/public/lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="/public/lib/include/css/bootstrap-glyphicons.css" />
<link rel="stylesheet" href="/public/lib/include/css/styles.css" />
<script src="/public/lib/include/js/modernizr-2.6.2.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Brand</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="<c:url value="/"/>">Home <span
							class="sr-only">(current)</span></a></li>
					<li><a href="#">Link</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">Dropdown <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">Separated link</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="#">One more separated link</a></li>
						</ul></li>
				</ul>
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
				<ul class="nav navbar-nav navbar-right">
					<sec:authorize access="isAnonymous()">
						<li><a href='<c:url value="/signup"/>'> <span
								class="glyphicon glyphicon-list-alt"> </span> Sign up
						</a></li>
						<li><a href="/login">Sing in <span
								class="glyphicon glyphicon-log-in"></span></a></li>
					</sec:authorize>
					<sec:authorize access="isAuthenticated()">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"><span class="glyphicon glyphicon-user"></span>
							<!-- The value of principal come from UserDetailsImpl -->
								<sec:authentication property="principal.user.name" /> <span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a
									href="/users/<sec:authentication property='principal.user.id' />"><span
										class="glyphicon glyphicon-user"></span> Profile</a></li>
								<li><c:url var="logoutUrl" value="/logout" /> <form:form
										id="logoutForm" action="${logoutUrl }" method="post">
									</form:form> <a href="#"
									onclick="document.getElementById('logoutForm').submit()"><span
										class="glyphicon glyphicon-log-out"></span>Sing out</a></li>
							</ul></li>
					</sec:authorize>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<!-- This part is used when a user want to access without verifying email -->
	<sec:authorize access="hasRole('ROLE_UNVERIFIED')">
		<div class="alert alert-warning alert-dismissable">
		  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
		  Your email id is unverified. <a href="/users/resend-verification-mail">Click here</a> to get the verification mail again.
		</div>
    </sec:authorize>
	
	<!-- This part is used for flashing message to user  -->
	<c:if test="${not empty flashMessage }">
		<div class="alert alert-${flashKind} alert-dismissible container">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			${flashMessage }
		</div>
	</c:if>