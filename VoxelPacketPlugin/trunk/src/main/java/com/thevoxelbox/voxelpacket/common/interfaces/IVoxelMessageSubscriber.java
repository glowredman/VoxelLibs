package com.thevoxelbox.voxelpacket.common.interfaces;

import com.thevoxelbox.voxelpacket.common.VoxelMessage;

/**
 * Interface for message subscribers on the client and server side
 * 
 * @author Adam Mummery-Smith
 */
public interface IVoxelMessageSubscriber
{
	/**
	 * Called when a subscribed message is received
	 * 
	 * @param publisher Publisher which is dispatching the event
	 * @param message Message which was received
	 */
	public abstract void receiveMessage(IVoxelMessagePublisher publisher, VoxelMessage message);

	/**
	 * Called when a class cast exception is thrown from ReceiveMessage, this usually indicates that the data payload
	 * was not in the expected format and the generic cast failed. This is provided simply as a robustness mechanism
	 * so that ClassCastExceptions are not thrown and a subscriber can decide whether it cares about invalid data types
	 * or not.
	 * 
	 * @param publisher Publisher which is dispatching the event
	 * @param message Message which was received
	 * @param ex ClassCastException which was thrown by ReceiveMessage
	 */
	public abstract void receiveMessageClassCastFailure(IVoxelMessagePublisher publisher, VoxelMessage message, ClassCastException ex); 
}
