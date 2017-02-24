package com.drivecloud.web.controller;

import javax.servlet.http.Cookie;

public class Common {
    public String CheckCookie(Cookie cookies[]) {
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                String name = cookies[i].getName();
                String value = cookies[i].getValue();
                System.out.println("cookies: " + cookies[i].getName() + ":" + cookies[i].getValue());
                if (name.equals("Username")) {
                    return value;
                }
            }
        }
        return null;
    }
}
