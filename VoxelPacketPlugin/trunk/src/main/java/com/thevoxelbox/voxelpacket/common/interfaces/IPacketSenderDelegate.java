package com.thevoxelbox.voxelpacket.common.interfaces;

/**
 * Delegate interface for functions which send packets
 * 
 * @author Adam Mummery-Smith
 *
 */
public interface IPacketSenderDelegate
{
	/**
	 * Delegate function, send the packet using this delegate
	 * 
	 * @param channel
	 * @param payload
	 */
	public abstract void sendPayload(String channel, byte[] payload);
}
