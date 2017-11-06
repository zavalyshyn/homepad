package pt.inesc.gsd.homepad.modules.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.CloudCallRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.CloudCallResponseGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class CloudCallService extends Component {

	private static final Logger LOGGER = Logger.getLogger(CloudCallService.class);

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(CloudCallRequestGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof CloudCallRequestGlobalEvent) {
			CloudCallRequestGlobalEvent ein = (CloudCallRequestGlobalEvent) event;
			int sourceId = ein.getSourceElementId();
			String params = ein.getParams();
			int statusCode = -1;
			
			if (ein.isMultipart()) {
				// Execute HTTP Post Request with a multipart file upload
				CloseableHttpClient httpclient = HttpClients.createDefault();
		        try {
		            //HttpPost httppost = new HttpPost("http://146.193.41.153:8090/");
		            HttpPost httppost = new HttpPost(params);

		            // byte b[]= new byte[1000];
		            ByteArrayBody frameBytes = new ByteArrayBody(ein.getPayload(), "frame.jpg");
		            
		            HttpEntity reqEntity = MultipartEntityBuilder.create()
		                    .addPart("file", frameBytes)
		                    .build();

		            httppost.setEntity(reqEntity);

		            CloseableHttpResponse response = httpclient.execute(httppost);
		            statusCode = response.getStatusLine().getStatusCode();
		            response.close();
					httpclient.close();
					
					// Send a response back to the requesting element
					if ((statusCode==200) || (statusCode==302)) {
						CloudCallResponseGlobalEvent eout = new CloudCallResponseGlobalEvent(
								ein, sourceId, true, statusCode);			
						sendOutgoingEvent(eout);
					} else {
						CloudCallResponseGlobalEvent eout = new CloudCallResponseGlobalEvent(
								ein, sourceId, false, statusCode);			
						sendOutgoingEvent(eout);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else { 
				// TODO perform normal HTTP request and check for desired method
			}
		}
	}
}