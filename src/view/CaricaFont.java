package view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class CaricaFont {
	
	public Font getFont(int size) {
		try {
	        InputStream inputStream = getClass().getResourceAsStream("/font/font.ttf");
	        assert inputStream != null;
	        return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont((float) size);
	    } catch (IOException | FontFormatException ignored) {
	    	System.out.println("no");
	    	return null;
	    }
	}
	
}
