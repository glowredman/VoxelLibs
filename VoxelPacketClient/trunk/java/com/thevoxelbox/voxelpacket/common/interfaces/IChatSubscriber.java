package com.thevoxelbox.voxelpacket.common.interfaces;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for objects which subscribe to chat events
 * 
 * @author Adam Mummery-Smith
 *
 */
public interface IChatSubscriber
{
	/**
	 * Called when the chat broker receives a new chat message. Should normally return FALSE or can return TRUE
	 * to cancel further processing of this chat packet (all subscribers will be notified under all circumstances)
	 * 
	 * @param originalMessage Original unmodified message
	 * @param chatMessage Inbound chat message with guessed player name removed
	 * @param sender Best guess of the player (in visible range) who sent the message. Null if guessing failed
	 * @param senderName Best guess of the player (who may not be in visible range) name who sent the message. Null if guessing failed.
	 * @return
	 */
	public abstract boolean receiveChatMessage(String originalMessage, String chatMessage, EntityPlayer sender, String senderName);
}
