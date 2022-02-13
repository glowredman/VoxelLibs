package com.thevoxelbox.voxelpacket.common.interfaces;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Interface for objects which can handle inbound packets
 * 
 * @author Adam Mummery-Smith
 */
public interface IVoxelPacketHandler
{
	/**
	 * Handle a new received packet. The handler is expected to rebuild the message by managing inbound packets
	 * and forming messages from the packet sequence.
	 * 
	 * @param packet
	 * @param sender
	 */
	public abstract void handleVoxelPacket(com.thevoxelbox.voxelpacket.common.VoxelPacket packet, EntityPlayerMP sender);
	
	/**
	 * Register a new data encoder. The encoder's ID must be unique and consistent between the client and server
	 * 
	 * @param encoder
	 * @return
	 */
	public abstract boolean registerEncoder(IVoxelPacketEncoder<?> encoder);

	/**
	 * Get the encoder for the specified data type ID (if one is registered)
	 * 
	 * @param dataType
	 * @return Matching encoder or null if a matching encoder is not registered
	 */
	public abstract IVoxelPacketEncoder<?> getEncoder(int dataType);
	
	/**
	 * Get the encoder most appropriate for encoding the supplied object. Returns null if a suitable
	 * encoder was not found.
	 * 
	 * @param dataObject Object which you wish to encode
	 * @return Matching encoder or null if no matching encoder is found
	 */
	public abstract IVoxelPacketEncoder<?> getEncoder(Object dataObject);
}
