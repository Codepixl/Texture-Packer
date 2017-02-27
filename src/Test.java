import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by aaron on 2/26/2017.
 */
public class Test {
	public static void main(String[] args) throws IOException {
		TexturePacker p = new TexturePacker(500, new File(System.getProperty("user.home"), "texturepacker").listFiles());
		p.compute();
		ImageIO.write(p.createImage(BufferedImage.TYPE_INT_ARGB), "png", new File("example.png"));
	}
}
