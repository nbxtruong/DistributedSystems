package com.drivecloud.web.controller;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import com.drivecloud.web.DAO.Impl.JdbcUserDAO;
import com.drivecloud.web.model.FileCloud;
import com.drivecloud.web.model.FolderCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Handles requests for the application file upload requests
 */

@Controller
public class FileUploadController {

//	private static final Logger logger = LoggerFactory
//			.getLogger(FileUploadController.class);

    /**
     * Upload single file using Spring Controller
     */
    @Autowired
    private JdbcFilecloudDAO fileDAO;
    @Autowired
    private JdbcUserDAO UserDao;

    private int userid;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView printWelcome(ModelAndView model, HttpServletRequest request) throws SQLException {
        Common mycommon = new Common();
        Cookie cookies[] = request.getCookies();
        String username = mycommon.CheckCookie(cookies);
        if (username == null) {
            model.setViewName("redirect:login");
            return model;
        }
        userid = UserDao.GetUserID(username);
        List<FolderCloud> ListFolder = fileDAO.ListFolder(0, userid);
        model.addObject("listFolderview", ListFolder);
        model.setViewName("upload");
        //model.addAttribute("message", "Spring 3 MVC Hello World");
        return model;

    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@RequestParam("name") String name,
                             @RequestParam("listSelect") int folderid,
                             @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

//				logger.info("Server File Location="
//						+ serverFile.getAbsolutePath());
                System.out.println("Server File Location =" + serverFile.getAbsolutePath());
                int cloudType = 3;
                String fileid = "";
                String filelink = "";
                if (cloudType == 1) {
                    GoogleDrive myGoogleDrive = new GoogleDrive();
                    myGoogleDrive.getAuthorizeInfo();
                    myGoogleDrive.RefreshToken();
                    myGoogleDrive.GuploadFile(serverFile.getAbsolutePath());
                    fileid = myGoogleDrive.UpdateFileName();
                    filelink = myGoogleDrive.GetFileLink();
                } else if (cloudType == 2) {
                    OneDrive onedrive = new OneDrive();
                    onedrive.getAuthorizeInfo();
                    onedrive.RefreshToken();
                    fileid = onedrive.GuploadFile(serverFile.getAbsolutePath());
                    filelink = onedrive.GetFileLink();
                } else if (cloudType == 3) {
                    dropbox mydrobox = new dropbox();
                    mydrobox.getAuthorizeInfo();
                    fileid = mydrobox.GuploadFile(serverFile.getAbsolutePath());
                    filelink = mydrobox.GetFileLink(fileid);
                }
                FileCloud fileCloud = new FileCloud(folderid, fileid, name, userid, filelink, cloudType, serverFile.length());
                fileDAO.insert(fileCloud);
                serverFile.delete();
                return "You successfully uploaded file=" + name;
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name
                    + " because the file was empty.";
        }
    }
}
