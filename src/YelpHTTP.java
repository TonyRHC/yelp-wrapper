import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class YelpHTTP {
	
	final static String CLIENT_ID = "";
	final static String CLIENT_SECRET = "";
	final static String ACCESS_TOKEN = "";
	
	public static void getToken() {
		HttpClient client = HttpClientBuilder.create().build();
	    HttpPost httppost = new HttpPost("https://api.yelp.com/oauth2/token");
	    try {
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
	        params.add(new BasicNameValuePair("client_id", CLIENT_ID));
	        params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
	        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));

	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(httppost);

	        JSONObject json_auth = new JSONObject(EntityUtils.toString(response.getEntity()));
	        String token = json_auth.getString("access_token");
	        System.out.println("Change the ACCESS_TOKEN variable with the following: ");
	        System.out.println(token);

	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public JSONObject query(String term, String location) {
		JSONObject firstBusiness = null;
		
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("term", term));
			params.add(new BasicNameValuePair("location", location));
			
			HttpClient client = HttpClientBuilder.create().build();
			String pString = URLEncodedUtils.format(params, "UTF-8");
			HttpGet httpget = new HttpGet("https://api.yelp.com/v3/businesses/search?" + pString);
			httpget.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
	
			HttpResponse response = client.execute(httpget);
			StatusLine sl = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			
			if ( sl.getStatusCode() == 200 ) {
		        JSONObject responseJSON = new JSONObject(EntityUtils.toString(entity));
		        JSONArray businesses = (JSONArray) responseJSON.get("businesses");
		        if(!businesses.isNull(0)) firstBusiness = (JSONObject) businesses.get(0);
		        
			}

		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return firstBusiness;
		
	}

}
