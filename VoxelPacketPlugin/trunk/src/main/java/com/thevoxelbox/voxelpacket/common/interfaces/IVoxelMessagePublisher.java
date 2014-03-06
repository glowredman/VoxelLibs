package com.thevoxelbox.voxelpacket.common.interfaces;

/**
 * Interface for objects which publish message events
 * 
 * @author Adam Mummery-Smith
 */
public interface IVoxelMessagePublisher
{
	/**
	 * Subscribe to the specified shortcode
	 * 
	 * @param subscriber Subscriber to add
	 * @param shortCode Shortcode to subscribe to or "*" to subscribe to all. Min 1 char, max 6 chars.
	 */
	public abstract IVoxelMessagePublisher subscribe(IVoxelMessageSubscriber subscriber, String shortCode);
	
	/**
	 * Unsubscribe from the specified shortcode
	 * 
	 * @param subscriber Subscriber to remove
	 * @param shortCode Shortcode to subscribe to or "*" to subscribe to all. Min 1 char, max 6 chars.
	 */
	public abstract IVoxelMessagePublisher unsubscribe(IVoxelMessageSubscriber subscriber, String shortCode);
}
