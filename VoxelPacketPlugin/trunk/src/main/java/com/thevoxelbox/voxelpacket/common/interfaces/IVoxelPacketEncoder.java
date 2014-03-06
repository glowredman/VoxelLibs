package com.thevoxelbox.voxelpacket.common.interfaces;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.exceptions.InvalidReplicationException;

/**
 * Interface for objects which can encode and decode (yes ok the interface name is misleading) data
 * transmitted via VoxelPackets. This is used in preference to just using native serialisation as it
 * can serialise with awareness of sending the data over a network link. Using a specific encoder
 * for complex types is preferred as it can be more efficient than serialisation if done correctly.
 * 
 * @author Adam Mummery-Smith
 *
 * @param <T> Generic type that this encoder can manipulate, used as a direct lookup when searching
 * for compatible encoders
 */
public interface IVoxelPacketEncoder<T>
{
	/**
	 * Get the data type ID assigned to this encoder, this is used on transmitted packets
	 * 
	 * @return
	 */
	public abstract int getDataTypeID();
	
	/**
	 * Get the encoded data for the supplied object
	 * 
	 * @param object
	 * @return
	 */
	public abstract byte[] encode(T object); 
	
	/**
	 * Decode the received data into the appropriate type
	 * 
	 * @param dataBuffer
	 * @return
	 * @throws InvalidReplicationException 
	 */
	public abstract T decode(ByteBuffer dataBuffer) throws InvalidReplicationException;
}
