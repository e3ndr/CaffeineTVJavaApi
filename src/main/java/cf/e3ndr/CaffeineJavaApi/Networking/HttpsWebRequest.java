package cf.e3ndr.CaffeineJavaApi.Networking;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsWebRequest {
	
	public static RequestState executeGet(String address) {
		try {
			URL url = new URL(address);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			
			if (con.getResponseCode() != 404) {
				InputStream in = (InputStream) con.getContent();
				byte[] data = new byte[in.available()];
				in.read(data);
				RequestState state = RequestState.VALID;
				
				state.store(new String(data));
				
				return state;
			}
		} catch (Exception e) {}
		
		return RequestState.INVALID;
	}
}
