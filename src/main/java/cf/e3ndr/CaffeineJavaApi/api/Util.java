package cf.e3ndr.CaffeineJavaApi.api;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Util {
	public static BufferedImage resizeImage(BufferedImage img, int desiredWidth, int desiredHeight) { 
		Image tmp = img.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);
	    
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		
		return dimg;
	}  
}
