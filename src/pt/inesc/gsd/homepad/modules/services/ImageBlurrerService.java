package pt.inesc.gsd.homepad.modules.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.ImageBlurrerRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.ImageBlurrerResponseGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class ImageBlurrerService extends Component {
	
	private static final Logger LOGGER = Logger.getLogger(ImageBlurrerService.class);
	
	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(ImageBlurrerRequestGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());

		if (event instanceof ImageBlurrerRequestGlobalEvent) {
			// parse input parameters
			ImageBlurrerRequestGlobalEvent ein = (ImageBlurrerRequestGlobalEvent) event;
			int sourceId = ein.getSourceElementId();
			
			// TODO: process the frame ein.getFrame()
			
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(ein.getFrame());
				BufferedImage image = ImageIO.read(bais);
				
				// copy the original to the result
		        BufferedImage result = image;
		        int height = image.getHeight();
		        int width = image.getWidth();
		        // modify the result image
		        int factor = 2; // blur factor. Higher = more blur
		        for (int x=0; x<width; x++) {
		            for (int y=0; y<height; y++) {
		                int sumRed = 0;
		                int sumGreen = 0;
		                int sumBlue = 0;
		                int numNeigh = 0;
		                // get the rgb of the neighbors
		                for (int i=x-factor; i <= x + factor; i++) {
		                    for (int j = y - factor; j <= y + factor; j++) {
		                        // discard the out of bound pixels
		                        if (i>=0 && j>=0 && i<width && j<height) {
		                            int rgb = image.getRGB(i, j);
		                            Color color = new Color(rgb);
		                            sumRed += color.getRed();
		                            sumGreen += color.getGreen();
		                            sumBlue += color.getBlue();
		                            numNeigh += 1;
		                        }
		                    }
		                }
		                int averRGB = new Color(sumRed/numNeigh,sumGreen/numNeigh,sumBlue/numNeigh).getRGB();
		                result.setRGB(x, y, averRGB);
		            }
		        }
		        
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        ImageIO.write(result, "jpg", baos);
				baos.flush();
				byte[] blurredFrame = baos.toByteArray();
				baos.close();
				
				// send out the response, assuming the user was recognized
				ImageBlurrerResponseGlobalEvent eout = new ImageBlurrerResponseGlobalEvent(
						ein, sourceId, blurredFrame);
				sendOutgoingEvent(eout);
				
			} catch (IOException e) {
				LOGGER.log(Logger.ER, "Error blurring the image");
			}
		}
	}

}
