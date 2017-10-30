package pt.inesc.gsd.homepad.server;

public interface GlobalEventDispatcher {

	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event);
	
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType);
}
