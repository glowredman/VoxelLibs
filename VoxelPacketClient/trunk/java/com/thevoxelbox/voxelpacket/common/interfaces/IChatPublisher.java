package com.thevoxelbox.voxelpacket.common.interfaces;

/**
 * Interface for objects which publish subscribable chat events
 * 
 * @author Adam Mummery-Smith
 */
public interface IChatPublisher
{
	/**
	 * Subscribe to the chat packet received event
	 * 
	 * @param subscriber
	 */
	public abstract void subscribe(IChatSubscriber subscriber);
	
	/**
	 * Unsubscribe from chat events
	 * 
	 * @param subscriber
	 */
	public abstract void unsubscribe(IChatSubscriber subscriber);
}
