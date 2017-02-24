package com.drivecloud.web.controller;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import com.drivecloud.web.DAO.Impl.JdbcUserDAO;
import com.drivecloud.web.model.FileCloud;
import com.drivecloud.web.model.FolderCloud;
import com.drivecloud.web.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private JdbcUserDAO UserDao;
    @Autowired
    private JdbcFilecloudDAO fileDAO;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String displayLogin(ModelMap model, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        UserModel userModel = new UserModel();
        Common mycommon = new Common();
        //model.addObject("loginBean", userModel);
        model.addAttribute("loginUser", userModel);
//		if(cookies!=null){
//			for (int i = 0; i < cookies.length; i++) {
//				String name = cookies[i].getName();
//				String value = cookies[i].getValue();
//		         System.out.println("cookies: "+cookies[i].getName()+":"+cookies[i].getValue());
        String Ucookies = mycommon.CheckCookie(cookies);
        if (Ucookies != null) {
            model.addAttribute("loggedInUser", Ucookies);
            return "redirect:listFile1?fid=0";
        }
//				}
//	        	//model.setViewName("welcome");
//
//		}
        //model.setViewName("login");

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView executeLogin(HttpServletResponse response, ModelAndView model, @ModelAttribute("loginUser") UserModel userModel) {
        //ModelAndView model= null;
        try {
            boolean isValidUser = UserDao.isValidUser(userModel.getUsername(), userModel.getPassword());
            if (isValidUser) {
                System.out.println("User Login Successful");
                //request.setAttribute("loggedInUser", userModel.getUsername());
                //model = new ModelAndView("welcome");
                //model.addObject("loggedInUser", userModel.getUsername());
                Cookie cookie = new Cookie("Username", userModel.getUsername());
                cookie.setMaxAge(2330);
                response.addCookie(cookie);
                int userid = UserDao.GetUserID(userModel.getUsername());
                List<FileCloud> ListFile = fileDAO.list(0, userid);
                List<FolderCloud> ListFolder = fileDAO.ListFolder(0, userid);
                System.out.println("List File");
                for (int i = 0; i < ListFile.size(); i++) {
                    System.out.println(ListFile.get(i).getFileName());
                }
                //model.addObject("pid",0);
                //model.addObject("listFolderview",ListFolder);
                //model.addObject("listFileview",ListFile);
                //model.setViewName("redirect:listFile");
                model.setViewName("redirect:listfile1?fid=0");
                return model;

            } else {
                //model = new ModelAndView("login");
                //model.addObject("loginBean", loginBean);
                model.addObject("message", "Invalid credentials!!");
                model.setViewName("login");
                return model;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String Logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                System.out.println("cookies: " + cookies[i].getName() + ":" + cookies[i].getValue());
                if (cookie.getName().equals("Username")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            //model.setViewName("welcome");

        }
        return "redirect:login";
    }

}
