package com.drivecloud.web.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.drivecloud.web.DAO.Impl.JdbcFilecloudDAO;
import com.drivecloud.web.model.FileCloud;

public class GoogleDrive {
	private String CLIENT_ID ="";
	private String CLIENT_SECRET ="";
	private String REFRESH_TOKEN ="";
	private String TOKEN ="";
	private String FILEID = "";
	private String FILENAME ="";
	public void getAuthorizeInfo()
	{
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(
			        "/home/quoc/Downloads/googledrive.json"));
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);
			CLIENT_ID =	(String) jsonObject.get("client_id");
			CLIENT_SECRET =	(String) jsonObject.get("client_secret");
			TOKEN =	(String) jsonObject.get("token");
			REFRESH_TOKEN =	(String) jsonObject.get("refeshToken");
			
 
		    System.out.println("Client_id: " + CLIENT_ID);
	        System.out.println("CLIENT_SECRET: " + CLIENT_SECRET);
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
	
	public void RefreshToken()
	{
		JSONParser parser = new JSONParser();

		String url = "https://www.googleapis.com/oauth2/v4/token";
		URL obj;
		try {
			obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			String urlParameters = "client_id="+CLIENT_ID+
									"&client_secret="+CLIENT_SECRET+
									"&refresh_token="+REFRESH_TOKEN+
									"&grant_type=refresh_token"
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
			TOKEN =	(String) jsonObject.get("access_token");
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
	
	public void GetListFile()
	{
		String url = "https://www.googleapis.com/drive/v2/files?access_token="+TOKEN;
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GuploadFile(String filepath)
	{
		JSONParser parser = new JSONParser();

		String url = "https://www.googleapis.com/upload/drive/v3/files?uploadType=media";
		URL obj;
		try {
			File fileinput = new File(filepath);
			FILENAME = fileinput.getName(); 
			obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization","Bearer "+TOKEN);
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
			FILEID =	(String) jsonObject.get("id");
			System.out.println("ID: " + FILEID);


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

	}
	
	public String UpdateFileName() throws ParseException
	{
		String url = "https://www.googleapis.com/drive/v3/files/"+FILEID+"?addParents=0B7RnMNGDUo5ES3FaWkIzZElETUU&removeParents=0ALRnMNGDUo5EUk9PVA";
		URL obj;
		JSONParser parser = new JSONParser();

		try {
			obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization","Bearer "+TOKEN);
			con.setRequestProperty("Content-Type","application/json");
			//con.setRequestProperty("addParents","0B7RnMNGDUo5ES3FaWkIzZElETUU");
			con.setDoOutput(true);
			String urlParameters = "{\"name\":\""+FILENAME+"\",\"addParents\":\"0B7RnMNGDUo5ES3FaWkIzZElETUU\"}";
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
			String fileid =	(String) jsonObject.get("id");
			System.out.println("Link: " + fileid);
			return fileid;

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
		return "";
	}
	@Autowired
	JdbcFilecloudDAO fileDAO;
	public int DeleteFile(String fileid)
	{
		String url = "https://www.googleapis.com/drive/v3/files/"+fileid;
		URL obj;
		JSONParser parser = new JSONParser();

		try {
			obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			con.setRequestProperty("Authorization","Bearer "+TOKEN);
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
			if(responseCode == 204) return 0;
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
	public String GetFileLink()
	{
		String url = "https://www.googleapis.com/drive/v3/files/"+FILEID+"?fields=webContentLink";
		URL obj;
		JSONParser parser = new JSONParser();

		try {
			obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization","Bearer "+TOKEN);
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
			Object obj1 = parser.parse(response.toString());

			JSONObject jsonObject = (JSONObject) obj1;
			String filelink =	(String) jsonObject.get("webContentLink");
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
	
	public int Authorization()
	{
		
		return 0;
	}
}


