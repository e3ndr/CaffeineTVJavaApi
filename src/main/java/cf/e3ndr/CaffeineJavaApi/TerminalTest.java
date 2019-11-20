package cf.e3ndr.CaffeineJavaApi;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;

public class TerminalTest {

	public static void main(String[] args) {
		String username = "Jessiex";
		
		if (args.length > 0) username = args[0];
		
		CaffeineProfile profile = new CaffeineProfile(username);
		
		System.out.println(profile.toString());
		
		if (profile.isValid()) {
			System.out.println("followers: " + profile.getFollowers());
			System.out.println("following: " + profile.getFollowing());
			
			try {
				new CaffeineStream(profile, new TestListener());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
