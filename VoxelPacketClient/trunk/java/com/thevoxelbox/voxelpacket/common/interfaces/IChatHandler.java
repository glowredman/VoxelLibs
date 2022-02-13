package com.thevoxelbox.voxelpacket.common.interfaces;

import net.minecraft.network.play.server.S02PacketChat;

/**
 * Interface for objects which can handle inbound chat
 * 
 * @author Adam Mummery-Smith
 */
public interface IChatHandler
{
	/**
	 * Handle a chat packet, the implementor can return TRUE to cancel the normal dispatch of the
	 * packet to the normal packet handler or downstream handler (in case of override).
	 * 
	 * @param packet Chat packet that was received
	 * @return True to cancel further processing of the packet. False to route the packet normally.
	 */
	public abstract boolean handleChatPacket(S02PacketChat packet, String message);
}
