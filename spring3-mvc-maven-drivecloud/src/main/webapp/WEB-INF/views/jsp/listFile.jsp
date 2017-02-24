<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Contact Manager Home</title>
          <meta name="viewport" content="width=device-width, initial-scale=1">
  		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="table-responsive">
            <h1>List File</h1>
            <a href="/newfolderform?pid=${pid}" class="btn btn-info" role="button">New folder</a>
            <a href="/upload" class="btn btn-info" role="button">Upload</a>
            
            <table class="table table-hover">
            <thead>
            <tr>
                <th></th>
                <th>Name</th>
                <th>Size</th>
                <th>Rename</th>
                <th>Delete</th>
                <th>Move</th>
                <th>Copy</th>
             </tr>
  			</thead>
  				 <tbody>
                <c:forEach var="folder" items="${listFolderview}">
                <tr>
                    <td>x</td>
                    <td><a href="listfile1?fid=${folder.getID()}">${folder.getName()}</a></td>
                    <td></td>
                    <td></td>
                    <td><a href="delfolder?folderid=${folder.getID()}">delete</a></td>
                    
                </tr>
                </c:forEach>  
                <c:forEach var="file" items="${listFileview}">
                <tr>
                    <td></td>

                    <td>
                    	<a href="${file.getLink()}">${file.getFileName()}</a>
                    </td> 
                    <td>
                    	${file.getSize()}
                    </td>
                    <td>
                    </td> 
                    <td>
                    	<a href="deletfile?fid=${file.getFileID()}&ctype=${file.getCloudType()}&flid=${file.getfolderID()}">delete</a>
                    </td>       
                </tr>
                </tbody>
                </c:forEach>            
            </table>
        </div>
        <div class="container">
</html>