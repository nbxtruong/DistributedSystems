package com.drivecloud.web.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class abc {
    @RequestMapping(value = "/abc", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-Module.xml");
        dropbox mydrobox = new dropbox();
        mydrobox.getAuthorizeInfo();
        String filename = mydrobox.GuploadFile("/home/quoc/Pictures/virtualbox-icon.png");
        String filelink = mydrobox.GetFileLink(filename);
        System.out.println(filelink);
//    	OneDrive onedrive = new OneDrive();
//    	onedrive.getAuthorizeInfo();
//    	onedrive.RefreshToken();
//    	onedrive.GuploadFile("/home/quoc/Pictures/virtualbox-icon.png");
//    	onedrive.GetFileLink();
//    	JdbcFilecloudDAO fileDAO = (JdbcFilecloudDAO) context.getBean("FileDAO");
//    	FileCloud fileCloud = new FileCloud(1,"1","1",1,"abc",1);
//    	fileDAO.insert(fileCloud);

//		model.addAttribute("message", "Spring 3 MVC Hello World");
//		GoogleDrive myGoogleDrive = new GoogleDrive();
//		myGoogleDrive.getAuthorizeInfo();
//		myGoogleDrive.RefreshToken();
//		myGoogleDrive.GuploadFile("/home/quoc/Desktop/nun/DSC_0007.JPG");
//		myGoogleDrive.UpdateFileName();
        return "hello";

    }
}
