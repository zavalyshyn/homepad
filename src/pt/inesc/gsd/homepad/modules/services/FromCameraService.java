package pt.inesc.gsd.homepad.modules.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import pt.inesc.gsd.homepad.modules.messages.FromCameraGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FromCameraService extends Component {

	private Thread _cameraPooler = null;
	private boolean _cameraPoolerRunning = false;
	
	private InputStream urlStream;
	private StringWriter stringWriter;
	private boolean processing = true;
	
//	private static final String STREAM_URL = "http://96.10.1.168/mjpg/1/video.mjpg";
//	private static final String STREAM_URL = "http://wmccpinetop.axiscam.net/mjpg/video.mjpg";
	private static final String STREAM_URL = "http://70.185.95.146/mjpg/video.mjpg";
	private static final String CONTENT_LENGTH = "Content-Length: ";

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		return false;
	}

	@Override
	public void enabling() {
		_cameraPoolerRunning = true;
		_cameraPooler = new Thread() {
		    public void run() {
	        	while(_cameraPoolerRunning) {
			        try {

			        	
			        	// Open the stream
						URL url = new URL(STREAM_URL);
						URLConnection urlConn = url.openConnection();
						// change the timeout to taste, I like 1 second
						urlConn.setReadTimeout(1000);
						urlConn.connect();
						urlStream = urlConn.getInputStream();
						stringWriter = new StringWriter(128);
						
						// crop the frame header
						boolean haveHeader = false; 
						int currByte = -1;
						String header = "";
						
						while((currByte = urlStream.read()) > -1 && !haveHeader) {
							stringWriter.write(currByte);
							
							String tempString = stringWriter.toString(); 
							
							// Count how many lines tempString contains
							String findStr = "\n";
							int lastIndex = 0;
							int count = 0;
							while(lastIndex != -1){
							    lastIndex = tempString.indexOf(findStr,lastIndex);
							    if(lastIndex != -1){
							        count ++;
							        lastIndex += findStr.length();
							    }
							}
							
							header = tempString;
							if (count==3) haveHeader = true; // each header consists of 3 lines before the frame bytes begin
						}
						
						
						// 255 indicates the start of the jpeg image
						while((urlStream.read()) != 255)
						{
							// just skip extras
						}
						
						// process the frame bytes
						int contentLength = contentLength(header);
						byte[] imageBytes = new byte[contentLength + 1];
						// since we ate the original 255 , shove it back in
						imageBytes[0] = (byte)255;
						int offset = 1;
						int numRead = 0;
						while (offset < imageBytes.length
							&& (numRead=urlStream.read(imageBytes, offset, imageBytes.length-offset)) >= 0) 
						{
							offset += numRead;
						}   
						
						stringWriter = new StringWriter(128);

			        	
		        	 sendOutgoingEvent(new FromCameraGlobalEvent(imageBytes));
		        	 Thread.sleep(1000);
			        	
			        } catch(InterruptedException v) {
			        	return;
			        } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
	        	}
		    }
		};
		_cameraPooler.start();
	}

	
	private static int contentLength(String header)
	{
		int indexOfContentLength = header.indexOf(CONTENT_LENGTH);
		int valueStartPos = indexOfContentLength + CONTENT_LENGTH.length();
		int indexOfEOL = header.indexOf('\n', indexOfContentLength);
		
		String lengthValStr = header.substring(valueStartPos, indexOfEOL).trim();
		
		int retValue = Integer.parseInt(lengthValStr);
		
		return retValue;
	}
	
	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
	}

	@Override
	public void disabling() {
		_cameraPoolerRunning = false;
		_cameraPooler.interrupt();
		try {
			_cameraPooler.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		_cameraPooler = null;
	}
}
