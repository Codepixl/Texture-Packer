import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by aaron on 2/26/2017.
 */
public class TexturePacker{
	private URL[] imageFiles;
	private Point[] imageLocations;
	private BufferedImage[] images;
	private int width, height;
	private ArrayList<Boolean[]> lines = new ArrayList<Boolean[]>();


	public TexturePacker(int width, URL[] images){
		this.imageFiles = images;
		this.width = width;
	}

	public TexturePacker(int width, File[] images) throws MalformedURLException {
		this.imageFiles = new URL[images.length];
		for(int i = 0; i < this.imageFiles.length; i++)
			this.imageFiles[i] = images[i].toURI().toURL();
		this.width = width;
	}

	public Point[] getImageLocations(){
		return imageLocations;
	}

	public void compute() throws IOException {
		loadImages();
		imageLocations = new Point[images.length];
		for(int j = 0; j < images.length; j++){
			BufferedImage i = images[j];
			int line = 0, xPos = 0;
			while(!roomForImage(i, line, xPos)){
				xPos++;
				if(xPos+i.getWidth() > width){
					xPos = 0;
					line++;
				}
			}
			for(int y = line; y < line + i.getHeight(); y++) {
				Boolean[] l = lines.get(y);
				for (int x = xPos; x < xPos + i.getWidth(); x++)
					l[x] = true;
			}
			imageLocations[j] = new Point(xPos, line);
		}
	}

	public BufferedImage createImage(int type){
		if(imageLocations == null) return null;
		BufferedImage img = new BufferedImage(width, lines.size(), type);
		Graphics2D g = img.createGraphics();
		for(int i = 0; i < imageLocations.length; i++)
			g.drawImage(images[i], imageLocations[i].x, imageLocations[i].y, null);
		return img;
	}

	private boolean roomForImage(BufferedImage i, int line, int xPos){
		if(xPos+i.getWidth() > width) return false;
		for(int y = line; y < line + i.getHeight(); y++) {
			Boolean[] l = getLine(y);
			for (int x = xPos; x < xPos + i.getWidth(); x++)
				if(l[x]) return false;
		}
		return true;
	}

	private Boolean[] getLine(int line){
		while(lines.size() < line+1){
			Boolean[] lineB = new Boolean[width];
			for(int i = 0; i < width; i++)
				lineB[i] = false;
			lines.add(lineB);
		}
		return lines.get(line);
	}

	private void loadImages() throws IOException{
		this.images = new BufferedImage[imageFiles.length];
		for(int i = 0; i < images.length; i++) {
			BufferedImage tmp = ImageIO.read(imageFiles[i]);
			if(tmp.getWidth() > width) System.err.println("Image "+ imageFiles[i]+" is too wide!");
			else images[i] = tmp;
		}
		Arrays.sort(images, (a,b) -> b.getWidth() - a.getWidth());
	}
}