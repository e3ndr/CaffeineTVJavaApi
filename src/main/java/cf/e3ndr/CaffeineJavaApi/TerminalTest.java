package cf.e3ndr.CaffeineJavaApi;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;

public class TerminalTest {

	public static void main(String[] args) {
		String username = "thedungeonrun";
		
		if (args.length > 0) {
			username = args[0];
		} else {
			System.out.println("Username not specified, using " + username + " for testing.");
		}
		
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
