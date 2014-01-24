package com.thevoxelbox.voxelpacket.common.interfaces;

import net.minecraft.network.Packet;

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
	 * @param packet Packet to send
	 */
	public abstract void sendPacket(Packet packet);
}
