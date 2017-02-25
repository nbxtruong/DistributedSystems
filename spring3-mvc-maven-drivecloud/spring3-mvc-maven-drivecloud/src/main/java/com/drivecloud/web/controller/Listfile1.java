package com.drivecloud.web.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import com.drivecloud.web.DAO.Impl.JdbcUserDAO;
import com.drivecloud.web.model.FileCloud;
import com.drivecloud.web.model.FolderCloud;
@Controller
public class Listfile1 {
	@Autowired
	private JdbcUserDAO UserDao;
	@Autowired
	private JdbcFilecloudDAO fileDAO;
		@RequestMapping(value = "/listfile2", method = RequestMethod.GET)
		public @ResponseBody String printWelcome(@RequestParam("fid") int folderid,
				HttpServletRequest request) throws SQLException {
			Common mycommon = new Common();
			Cookie cookies[]=request.getCookies(); 
			String username = mycommon.CheckCookie(cookies);
			System.out.println("User Name :"+username);
			int userid =UserDao.GetUserID(username);
			System.out.println("User id :"+userid);
			//if(folderid == null) folderid = 0;
	    	List<FileCloud> ListFile = fileDAO.listallfile(userid);
	    	List<FolderCloud> ListFolder = fileDAO.ListAllFolder(userid);
	    	System.out.println("List File");
	    	JSONObject obj = new JSONObject();
	    	JSONObject obj1 = new JSONObject();
	    	obj1.put("SearchQuery","");
	    	obj1.put("Column","Title");
	    	obj1.put("Direction","directions.ascending");
	    	obj.put("GridOptions", obj1);
	    	JSONArray FolderJson = new JSONArray();
	    	JSONArray FileJson = new JSONArray();

	    	for(int i=0;i<ListFolder.size();i++){
	    	    System.out.println(ListFolder.get(i).getName());
	    	    JSONObject objtmp = new JSONObject();
	    	    objtmp.put("Id", ListFolder.get(i).getID());
	    	    objtmp.put("Title", ListFolder.get(i).getName());
	    	    String pid = ""+ListFolder.get(i).getPID();
	    	    
	    	    objtmp.put("ParentId",ListFolder.get(i).getPID()==0?"":pid);
	    	    FolderJson.add(objtmp);
	    	}
	    	for(int i=0;i<ListFile.size();i++){
	    	    System.out.println(ListFile.get(i).getFileName());
	    	    JSONObject objtmp1 = new JSONObject();
	    	    objtmp1.put("Id", ListFile.get(i).getFileID());
	    	    objtmp1.put("Title", ListFile.get(i).getFileName());
	    	    String foid = ""+ListFile.get(i).getfolderID();
	    	    
	    	    objtmp1.put("FolderId",ListFile.get(i).getfolderID()==0?"":foid);
	    	    FileJson.add(objtmp1);
	    	}
	    	obj.put("Folders", FolderJson);
	    	obj.put("Files", FileJson);
			return obj.toJSONString();
		}
}
