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

public class dropbox {
    @Autowired
    JdbcFilecloudDAO fileDAO;
    private String oauth1_token = "";
    private String oauth1_token_secret = "";
    private String TOKEN = "";
    private String FILEID = "";
    private String FILENAME = "";

    public void getAuthorizeInfo() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(
                    "../webapp/resources/token/dropbox.json"));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);
            oauth1_token = (String) jsonObject.get("oauth1_token");
            oauth1_token_secret = (String) jsonObject.get("oauth1_token_secret");
            TOKEN = (String) jsonObject.get("token");


            System.out.println("Client_id: " + oauth1_token);
            System.out.println("CLIENT_SECRET: " + oauth1_token_secret);
            System.out.println("TOKEN: " + TOKEN);
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

        String url = "https://api.dropboxapi.com/2/auth/token/from_oauth1";
        URL obj;
        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer XzXgZOhnTw0AAAAAAAAAExtXNFzxiGJ7x-wusacHkIKII894GzQKCQmZ08nYD3gK");

            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");
            String urlParameters = "{\"oauth1_token\":\"" + oauth1_token + "\"" +
                    ",\"oauth1_token_secret\":\"" + oauth1_token_secret + "\"}";
            ;
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
            TOKEN = (String) jsonObject.get("oauth2_token");
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
            String url = "https://content.dropboxapi.com/2/files/upload";

            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            con.setRequestProperty("Dropbox-API-Arg", "{\"path\": \"/testapp/" + FILENAME + "\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}");
            con.setRequestProperty("Content-Type", "application/octet-stream");
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

            return FILENAME;
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

    public String GetFileLink(String FileName) {
        String url = "https://api.dropboxapi.com/2/sharing/create_shared_link_with_settings";
        URL obj;
        JSONParser parser = new JSONParser();

        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            con.setRequestProperty("Content-Type", "application/json");
            String urlParameters = "{\"path\": \"/testapp/" + FileName + "\",\"settings\": {\"requested_visibility\": \"public\"}}";
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
            String filelink = (String) jsonObject.get("url").toString();
            return filelink.replace("dl=0", "dl=1");

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
        String url = "https://api.dropboxapi.com/2/files/delete";
        URL obj;
        JSONParser parser = new JSONParser();

        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
            //con.setRequestProperty("Content-Type","application/json");

            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            String urlParameters = "{\"path\": \"/testapp/" + fileid + "\"}";
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
            if (responseCode == 200) return 0;
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
