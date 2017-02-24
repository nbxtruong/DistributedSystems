package com.drivecloud.web.controller;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneDrive {
    @Autowired
    JdbcFilecloudDAO fileDAO;
    private String CLIENT_ID = "";
    private String REFRESH_TOKEN = "";
    private String TOKEN = "";
    private String FILEID = "";
    private String FILENAME = "";
    private String DIURL = "";

    public void getAuthorizeInfo() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(
                    "../webapp/resources/token/onedrive.json"));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);
            CLIENT_ID = (String) jsonObject.get("client_id");
            TOKEN = (String) jsonObject.get("access_token");
            REFRESH_TOKEN = (String) jsonObject.get("refresh_token");
            DIURL = (String) jsonObject.get("redirect_uri");

            System.out.println("Client_id: " + CLIENT_ID);
            System.out.println("DIURL: " + DIURL);
            System.out.println("TOKEN: " + TOKEN);
            System.out.println("REFRESH_TOKEN: " + REFRESH_TOKEN);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void RefreshToken() {
        JSONParser parser = new JSONParser();

        String url = "https://login.live.com/oauth20_token.srf";
        URL obj;
        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            String urlParameters = "client_id=" + CLIENT_ID +
                    "&redirect_uri" + DIURL +
                    "&refresh_token=" + REFRESH_TOKEN +
                    "&grant_type=refresh_token";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            Object obj1 = parser.parse(response.toString());
            JSONObject jsonObject = (JSONObject) obj1;
            TOKEN = (String) jsonObject.get("access_token");
            System.out.println("TOKEN: " + TOKEN);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String GuploadFile(String filepath) {
        JSONParser parser = new JSONParser();

        //String url = "https://api.onedrive.com/v1.0/drive/items/D363CF789B50E273%21110538:/";
        URL obj;
        try {
            File fileinput = new File(filepath);
            DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            Date date = new Date();
            String strDate = dateFormat.format(date);
            FILENAME = strDate + fileinput.getName();
            String url = "https://api.onedrive.com/v1.0/drive/items/D363CF789B50E273%21110538:/" + FILENAME + ":/content";

            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            FileInputStream fin = new FileInputStream(filepath);
            int c = 0;
            byte[] buf = new byte[8192];
            while ((c = fin.read(buf, 0, buf.length)) > 0) {
                wr.write(buf, 0, c);
                wr.flush();
            }
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());

            Object obj1 = parser.parse(response.toString());

            JSONObject jsonObject = (JSONObject) obj1;
            FILEID = (String) jsonObject.get("id");
            System.out.println("ID: " + FILEID);

            return FILEID;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

    public String GetFileLink() {
        String url = "https://api.onedrive.com/v1.0/drive/items/" + FILEID + "/action.createLink";
        URL obj;
        JSONParser parser = new JSONParser();

        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            con.setRequestProperty("Content-Type", "application/json");
            String urlParameters = "{\"type\":\"view\",\"scope\":\"anonymous\"}";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            Object obj1 = parser.parse(response.toString());

            JSONObject jsonObject = (JSONObject) obj1;
            //JSONArray jsonMainArr = new JSONArray(jsonObject("source"));
            String filelink = (String) jsonObject.get("link").toString();
            obj1 = parser.parse(filelink);
            jsonObject = (JSONObject) obj1;
            filelink = (String) jsonObject.get("webUrl").toString();
            System.out.println("Link: " + filelink);
            return filelink;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public int DeleteFile(String fileid) {
        String url = "https://api.onedrive.com/v1.0/drive/items/" + fileid;
        URL obj;
        JSONParser parser = new JSONParser();

        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            //con.setRequestProperty("Content-Type","application/json");
            //con.setDoOutput(true);
            //String urlParameters = "{\"name\":\""+FILENAME+"\"}";
            //con.setDoOutput(true);
            //DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            //wr.writeBytes(urlParameters);
            //wr.flush();
            //wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            if (responseCode == 204) return 0;
//			Object obj1 = parser.parse(response.toString());

//			JSONObject jsonObject = (JSONObject) obj1;
//			String filelink =	(String) jsonObject.get("webContentLink");
//			System.out.println("Link: " + filelink);
//			return filelink;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
}
