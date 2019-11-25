package cf.e3ndr.CaffeineJavaApi.api;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Util {
	public static BufferedImage resizeImage(BufferedImage img, int desiredWidth, int desiredHeight) { 
		Image tmp = img.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);
	    
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		
		return dimg;
	}
	
	public static JSONObject readJsonFile(File file) throws Exception {
		String jsonFile = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		
		if (jsonFile.length() == 0) return new JSONObject();
		
		return (JSONObject) new JSONParser().parse(jsonFile);
	}
	
	public static void writeJsonFile(File file, JSONObject json, boolean append) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(file, append));
		
		pw.write(json.toJSONString());
		
		pw.close();
	}

	public static int getIntOrDefault(JSONObject json, String key, int defaultValue) {
		Object value = json.get(key);
		
		return (value == null) ? defaultValue : ((Long) value).intValue();
	}
	
	public static String getStringOrDefault(JSONObject json, String key, String defaultValue) {
		Object value = (String) json.get(key);
		
		return (value == null) ? defaultValue : ((String) value);
	}
	
	public static boolean getBooleanOrDefault(JSONObject json, String key, boolean defaultValue) {
		Object value = (Boolean) json.get(key);
		
		return (value == null) ? defaultValue : ((Boolean) value);
	}
	
}
