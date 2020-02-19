package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import window.Window;

public class ImageUtils {

	// converts an Image to a BufferedImage
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bi.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bi;
	}

	// checks if certain co-ord on map is blue i.e. is water
	public static boolean isWater(int x, int y) {
		int clr = Window.originalMapImage.getRGB(x, y);
		int blue = clr & 0x000000ff;
		return blue == 255;
	}

}
