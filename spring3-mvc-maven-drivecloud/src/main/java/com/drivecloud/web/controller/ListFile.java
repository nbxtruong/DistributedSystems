package com.drivecloud.web.controller;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import com.drivecloud.web.DAO.Impl.JdbcUserDAO;
import com.drivecloud.web.model.FileCloud;
import com.drivecloud.web.model.FolderCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ListFile {
    @Autowired
    private JdbcFilecloudDAO fileDAO;
    @Autowired
    private JdbcUserDAO UserDao;

    @RequestMapping(value = "/listfile1", method = RequestMethod.GET)
    //public ModelAndView getlistfile (ModelAndView model) {
    public ModelAndView getlistfile(ModelAndView model,
                                    HttpServletRequest request,
                                    @RequestParam("fid") int folderid) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            model.setViewName("redirect:login");
            return model;
        }
        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        System.out.println("User id :" + userid);
        //if(folderid == null) folderid = 0;
        List<FileCloud> ListFile = fileDAO.list(folderid, userid);
        List<FolderCloud> ListFolder = fileDAO.ListFolder(folderid, userid);
        System.out.println("List File");
        for (int i = 0; i < ListFile.size(); i++) {
            System.out.println(ListFile.get(i).getFileName());
        }
        model.addObject("pid", folderid);
        model.addObject("listFolderview", ListFolder);
        model.addObject("listFileview", ListFile);
        model.setViewName("listFile");
        return model;
//		return "listFile";

    }

    @RequestMapping(value = "/deletfile", method = RequestMethod.GET)
    public String getlistfile(@RequestParam("fid") String fileid,
                              @RequestParam("ctype") int ctype,
                              @RequestParam("flid") String folderid,
                              HttpServletRequest request,
                              ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }
        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        if (ctype == 1) {
            GoogleDrive myGoogleDrive = new GoogleDrive();
            myGoogleDrive.getAuthorizeInfo();
            myGoogleDrive.RefreshToken();
            myGoogleDrive.DeleteFile(fileid);
        } else if (ctype == 2) {
            OneDrive onedrive = new OneDrive();
            onedrive.getAuthorizeInfo();
            onedrive.RefreshToken();
            onedrive.DeleteFile(fileid);
        } else if (ctype == 3) {
            dropbox mydropbox = new dropbox();
            mydropbox.getAuthorizeInfo();
            mydropbox.DeleteFile(fileid);
        }
        fileDAO.DeleFile(userid, fileid);


        return "redirect:listfile1?fid=" + folderid;
    }

    @RequestMapping(value = "/movefile", method = RequestMethod.GET)
    public String reqmovefile(@RequestParam("fid") String fileid,
                              @RequestParam("folderid") int folderid,
                              HttpServletRequest request,
                              ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }
        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        fileDAO.ChangeFFolderID(fileid, folderid, userid);
        return "redirect:listfile1?fid=" + folderid;
    }

    @RequestMapping(value = "/renamefile", method = RequestMethod.GET)
    public String reqrenamefile(@RequestParam("fid") String fileid,
                                @RequestParam("filename") String filename,
                                @RequestParam("folderid") int folderid,
                                HttpServletRequest request,
                                ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }
        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        fileDAO.ChangeFilename(fileid, filename, userid);
        return "redirect:listfile1?fid=" + folderid;
    }

    @RequestMapping(value = "/delfolder", method = RequestMethod.GET)
    public String deletefolder(
            @RequestParam("folderid") int folderid,
            HttpServletRequest request,
            ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }
        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        fileDAO.DeleteFolder(userid, folderid);
        fileDAO.DeleFileWithfolder(userid, folderid);
        return "redirect:listfile1?fid=" + folderid;
    }

    @RequestMapping(value = "/newfolder", method = RequestMethod.GET)
    public String newfolder(
            @RequestParam("name") String foldername,
            @RequestParam("pid") int pid,
            HttpServletRequest request,
            ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }

        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        FolderCloud folderCloud = new FolderCloud(0, pid, userid, foldername);
        fileDAO.NewFolder(folderCloud);
        return "redirect:listfile1?fid=" + pid;
    }

    @RequestMapping(value = "/newfolderform", method = RequestMethod.GET)
    public String newfolderform(
            @RequestParam("pid") int pid,
            HttpServletRequest request,
            ModelMap model) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            return ("redirect:login");
        }

        System.out.println("User Name :" + username);
        int userid = UserDao.GetUserID(username);
        model.addAttribute("pid", pid);
        return "newFolder";
    }
}
