package com.thevoxelbox.common.interfaces;

import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

import com.thevoxelbox.common.entity.EntityOtherPlayerMPHookable;

/**
 *
 * @author Adam Mummery-Smith
 */
public interface IEntityOtherPlayerMPSpawnListener
{
	/**
	 * @param packet
	 */
	public abstract void onPreSpawnEntity(S0CPacketSpawnPlayer packet);
	
	/**
	 * @param packet
	 * @param other
	 */
	public abstract void onPostSpawnEntity(S0CPacketSpawnPlayer packet, EntityOtherPlayerMPHookable other);
}
