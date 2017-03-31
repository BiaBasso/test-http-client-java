package br.com.java.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class WeatherApiResponse {

	private final String USER_AGENT = "Mozilla/5.0";

	public String GetResponse() throws ClientProtocolException, IOException {
		
		StringBuffer result = new StringBuffer();
		
		HttpClient client = new DefaultHttpClient();
		
		String url = "http://url";
		
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/json");
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("username", "username"));
		urlParameters.add(new BasicNameValuePair("password", "password"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		HttpResponse response = client.execute(post);
		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code: " + responseCode);
		
		try {
			if (responseCode == 200)

			{
				System.out.println("Get Response is Successfull");
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				String line = "";
				
				while ((line = reader.readLine()) != null) {
					result.append(line);
					System.out.println(result.toString());
				}
			}
			return result.toString();

		} catch (Exception ex) {
			result.append("Get Response Failed");
			return result.toString();
		}

	}
}