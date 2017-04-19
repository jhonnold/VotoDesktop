package testing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Created by nicholasyamahamanschweikart on 3/13/17.
 * Test class
 */

public class Media {
	private static final String TAG = "media";
	// Image Stuff
	private byte imgID;
	private byte[] imgBuffer;
	private int imgSize, cursor, totalPackets, expectingPacketNumber;
	private boolean ready = false;
	BufferedImage img;
	private int imgIndex;
	// TODO and Question fields like correct answer

	Media(byte imgID, int totalPackets, int imgLength, int index) {
		this.imgID = imgID;
		this.totalPackets = totalPackets;
		this.imgSize = imgLength;
		imgBuffer = new byte[imgLength];
		expectingPacketNumber = 1;
		cursor = 0;
		this.imgIndex = index;
	}

	public void appendData(byte[] data) {
		// Copy in the data
		imgBuffer = append(imgBuffer, data, cursor);
		// Increment the position cursor and the expected packet.
		cursor += data.length;

		expectingPacketNumber += 1;

		// Flag Media ready if we have gotten all the packets.
		if (expectingPacketNumber > totalPackets) {
			try {
				img = ImageIO.read(new ByteArrayInputStream(imgBuffer));
				ImageIO.write(img, "jpg", new File("received" + imgIndex + ".jpg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			ready = true;
		}
	}

	public boolean isReady() {
		return ready;
	}

	public BufferedImage getImage() {
		if (ready)
			return img;
		return null;
	}

	public byte getImgID() {
		return imgID;
	}

	public int getExpectingPacketNumber() {
		return expectingPacketNumber;
	}
	
	private byte[] append(byte[] data, byte[] addition, int cursor) {
		for (int i = 0; i < addition.length; i++) {
			data[i + cursor] = addition[i];
		}
		
		return data;
	}
}
