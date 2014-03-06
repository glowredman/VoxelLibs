package com.thevoxelbox.voxelpacket.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelpacket.common.data.VoxelPacketCharset;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketHandler;
import com.thevoxelbox.voxelpacket.exceptions.InvalidShortcodeException;

/**
 * VoxelPackets pack abstract data types into sign update packets
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPacket
{
	public static final String VOXELPACKET_CHANNEL = "VOXELPACKET";

	/**
	 * Character set which is used to provide the encoders between string and byte arrays without mangling the data
	 */
	public static final VoxelPacketCharset CHARSET = new VoxelPacketCharset();
	
	/**
	 * Length of the packet header used in packet count calculations
	 */
	protected static final int HEADER_LENGTH = 18;
	
	/**
	 * Packet handler instance which handles inbound packets
	 */
	protected static IVoxelPacketHandler packetHandler;
	
	/**
	 * Next message ID
	 */
	protected static int nextMessageId = 0;
	
	/**
	 * Message ID
	 */
	public int packetMessageId;
	
	/**
	 * Data type index, only valid on the first message
	 */
	public int packetDataType;
	
	/**
	 * The shortcode for this packet
	 */
	public String packetShortCode;
	
	/**
	 * Relevant entity in this packet
	 */
	public Entity relevantEntity;
	
	public int messageLength;
	
	/**
	 * Data in this packet (payload only)
	 */
	public byte[] packetData;
	
	/**
	 * Intrinsic lock for the static buffers
	 */
	protected static Object bufferLock = new Object();
	
	/**
	 * Data buffer used when building the packet
	 */
	protected static ByteBuffer dataBuffer = ByteBuffer.allocate(65535);
	
	public VoxelPacket(String channel, int length, byte[] data)
	{
		if (channel.equals(VOXELPACKET_CHANNEL))
		{
			try
			{
				synchronized (bufferLock)
				{
					dataBuffer.clear();
					dataBuffer.put(data);
					dataBuffer.flip();
					
					this.packetDataType = dataBuffer.getInt();
					this.messageLength  = dataBuffer.getInt();
					this.relevantEntity = Util.getEntityWithID(dataBuffer.getInt());
					
					byte[] shortCodeBytes = new byte[6];
					dataBuffer.get(shortCodeBytes);

					this.packetShortCode = new String(shortCodeBytes, "US-ASCII").trim();

					this.packetData = new byte[dataBuffer.remaining()];
					dataBuffer.get(this.packetData);
				}
			}
			catch (UnsupportedEncodingException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void handle(Player sender)
	{
		packetHandler.handleVoxelPacket(this, sender);
	}
	
	public static byte[] createPayload(int dataType, Entity relevantEntity, String shortCode, byte[] payload) throws InvalidShortcodeException
	{
		// Get next valid message id
		int messageId = nextMessageId++;
		
		if (shortCode.length() > 6 || shortCode.length() < 1)
			throw new InvalidShortcodeException("Invalid shortcode specified building voxel packet with ID " + messageId);

		// Index into the payload array
		int messageLength = payload.length;
		
		synchronized (bufferLock)
		{
			// Initialise the databuffer with the payload data
			dataBuffer.clear();
			dataBuffer.putInt(dataType);
			dataBuffer.putInt(messageLength);
			dataBuffer.putInt((relevantEntity != null) ? relevantEntity.getEntityId() : -1);

			// Build the header
			try
			{
				dataBuffer.put(shortCode.getBytes("US-ASCII"));
			}
			catch (UnsupportedEncodingException ex) 
			{
				throw new IllegalArgumentException("VoxelPacket error: Shortcode contains invalid (non-ASCII) characters at ID " + messageId);
			}

			while (dataBuffer.position() < (HEADER_LENGTH))
			{
				dataBuffer.put((byte)32);
			}
			
			dataBuffer.put(payload);
			int length = dataBuffer.position();
			dataBuffer.flip();
			
			byte[] packetData = new byte[length];
			dataBuffer.get(packetData);
			return packetData;
		}
	}		
    
	/**
	 * Register the specified handler as the packet handler for this packet
	 * @param handler
	 */
	public static void registerPacketHandler(IVoxelPacketHandler handler)
	{
		packetHandler = handler;
	}
}
