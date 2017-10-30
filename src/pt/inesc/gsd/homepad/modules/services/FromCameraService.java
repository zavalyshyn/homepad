package pt.inesc.gsd.homepad.modules.services;

import pt.inesc.gsd.homepad.modules.messages.FromCameraGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FromCameraService extends Component {

	private Thread _cameraPooler = null;
	private boolean _cameraPoolerRunning = false;

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
			        	Thread.sleep(1000);
			        	sendOutgoingEvent(new FromCameraGlobalEvent(new byte[100]));
			        } catch(InterruptedException v) {
			        	return;
			        }
	        	}
		    }
		};
		_cameraPooler.start();
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
