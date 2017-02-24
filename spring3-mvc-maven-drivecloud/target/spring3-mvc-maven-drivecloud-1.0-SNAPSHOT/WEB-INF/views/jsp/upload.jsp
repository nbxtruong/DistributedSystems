<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
  		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<title>Upload File Request Page</title>
</head>
<body>
	<form class="form-inline" method="POST" action="uploadFile" enctype="multipart/form-data">
	 <div class="form-group">
		File to upload: <input class="form-control" type="file" name="file">
	</div>
	<div class="form-group">
		Name: <input class="form-control" type="text" name="name">
	</div>
	<div class="form-group">
		Folder: <select class="form-control" name='listSelect'>
		    <option value='0'>current</option>
			<c:forEach items="${listFolderview}" var="temp">
    			<option value='${temp.getID()}'>${temp.getName()}</option>
			</c:forEach>
		</select>
	</div>
		<input class="btn btn-default" type="submit" value="Upload"> Press here to upload the file!
	</form>	
</body>
</html>