package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.Component;

public class Position {
	int x;
	int y;
	int w;
	int h;
	
	public Position(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Position(Component comp) {
		this(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("x:");
		sb.append(this.x);
		sb.append(" y:");
		sb.append(this.y);
		sb.append(" w:");
		sb.append(this.w);
		sb.append(" h:");
		sb.append(this.h);
		
		return sb.toString();
	}
}
