package CLIENT.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import javax.imageio.ImageIO;

public class CaricaImmagine {
	
	public CaricaImmagine() {}
	
	public BufferedImage immagine(String nome) {
		BufferedImage style = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/CLIENT/immagini/"+nome+".png");
            style = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return style;
	}
	
}
