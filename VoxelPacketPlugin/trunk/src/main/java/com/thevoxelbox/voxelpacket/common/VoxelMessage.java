package com.thevoxelbox.voxelpacket.common;

import java.nio.ByteBuffer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketHandler;
import com.thevoxelbox.voxelpacket.exceptions.InvalidReplicationException;
import com.thevoxelbox.voxelpacket.exceptions.MessageOverflowException;
import com.thevoxelbox.voxelpacket.exceptions.MissingEncoderException;

/**
 * Encapsulates an entire message which is received by the agent, used mainly to aggregate multiple inbound
 * packets into a single coherent message.
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelMessage
{
	/**
	 * Locally-routed messages do not have a message ID from the remote endpoint, local messages are therefore
	 * assigned a special ID starting at -1 and working down
	 */
	private static int localMessageId = -1;
	
	/**
	 * True if locally routed message
	 */
	private final boolean isLocal;
	
	/**
	 * Shortcode of this message, read-only
	 */
	private final String shortCode;
	
	/**
	 * ID of this message, read-only
	 */
	private final int messageId;
	
	/**
	 * Length of the message payload
	 */
	private final int messageLength;
	
	/**
	 * Sender
	 */
	private final Player player;
	
	/**
	 * Message relevant entity (if any)
	 */
	private final Entity relevantEntity;
	
	/**
	 * Data type ID for decoding received data
	 */
	private final int dataType;
	
	/**
	 * Received and decoded message data
	 */
	private Object messageData;

	/**
	 * Temporary buffer used to store inbound packet data as it is received
	 */
	private ByteBuffer dataBuffer;
	
	/**
	 * True if all packets have been received and decoded
	 */
	private boolean finalised = false;
	
	/**
	 * Packet handler, only used to retrieve the decoder
	 */
	private IVoxelPacketHandler agent;
	
	/**
	 * Create a new message using the specified packet data. The message will be immediately finalised
	 * if the message is comprised of only a single packet
	 * 
	 * @param agent Packet handler agent
	 * @param packet Received packet
	 * @throws MissingEncoderException Thrown if the inbound packet has an invalid data type ID
	 * @throws MessageOverflowException Thrown if the inbound packet has a higher index than the maximum packet specified.
	 * @throws InvalidReplicationException 
	 */
	public VoxelMessage(IVoxelPacketHandler agent, VoxelPacket packet) throws MissingEncoderException, MessageOverflowException, InvalidReplicationException
	{
		this.agent          = agent;
		this.player         = null;
		this.isLocal        = false; 
		this.shortCode      = packet.packetShortCode;
		this.messageId      = packet.packetMessageId;
		this.messageLength  = packet.messageLength;
		this.dataType       = packet.packetDataType;
		this.relevantEntity = packet.relevantEntity;
		this.dataBuffer     = ByteBuffer.allocate(packet.messageLength);
		
		this.dataBuffer.put(packet.packetData);
		this.finalise();
	}
	
	/**
	 * Create a new message using the specified packet data. The message will be immediately finalised
	 * if the message is comprised of only a single packet
	 * 
	 * @param agent Packet handler agent
	 * @param packet Received packet
	 * @throws MissingEncoderException Thrown if the inbound packet has an invalid data type ID
	 * @throws MessageOverflowException Thrown if the inbound packet has a higher index than the maximum packet specified.
	 * @throws InvalidReplicationException 
	 */
	public VoxelMessage(IVoxelPacketHandler agent, VoxelPacket packet, Player sender) throws MissingEncoderException, MessageOverflowException, InvalidReplicationException
	{
		this.agent          = agent;
		this.player         = sender;
		this.isLocal        = false; 
		this.shortCode      = packet.packetShortCode;
		this.messageId      = packet.packetMessageId;
		this.messageLength  = packet.messageLength;
		this.dataType       = packet.packetDataType;
		this.relevantEntity = packet.relevantEntity;
		this.dataBuffer     = ByteBuffer.allocate(packet.messageLength);
		
		this.dataBuffer.put(packet.packetData);
		this.finalise();
	}

	/**
	 * Create a new local message with no payload and the specified shortcode (used only for signalling)
	 *  
	 * @param shortCode
	 */
	public VoxelMessage(String shortCode)
	{
		this(shortCode, null, null);
	}
	
	/**
	 * Create a new local message with the specified shortcode and data
	 * 
	 * @param shortCode
	 * @param data
	 */
	public VoxelMessage(String shortCode, Object data)
	{
		this(shortCode, data, null);
	}
	
	/**
	 * Create a new local message with the specified shortcode, data and relevant entity
	 * 
	 * @param shortCode
	 * @param data
	 * @param relevantEntity
	 */
	public VoxelMessage(String shortCode, Object data, Entity relevantEntity)
	{
		this.player         = null;
		this.isLocal        = true; 
		this.dataType       = 0;
		this.messageLength  = 0;
		this.messageId      = localMessageId--;
		this.shortCode      = shortCode;
		this.relevantEntity = relevantEntity;
		this.messageData    = data;
		this.finalised      = true;
	}
	
	/**
	 * All packets were received, finalise the message and decode the data
	 * 
	 * @throws MissingEncoderException Thrown if the inbound packet has an invalid data type ID
	 * @throws InvalidReplicationException 
	 */
	protected void finalise() throws MissingEncoderException, InvalidReplicationException
	{
		this.dataBuffer.flip();
		this.finalised = true;

		IVoxelPacketEncoder<?> encoder = this.agent.getEncoder(this.dataType);
		
		if (encoder == null)
		{
			throw new MissingEncoderException("Could not find an encoder matching data type ID " + this.dataType);
		}
		
		while (this.dataBuffer.remaining() > this.messageLength)
		{
			int limit = this.dataBuffer.limit() - 1;
			this.dataBuffer.limit(limit);
		}

		this.messageData = encoder.decode(this.dataBuffer);
	}

	/**
	 * Returns true if the message is finalised and data can be safely read
	 * @return
	 */
	public boolean isFinalised()
	{
		return this.finalised;
	}
	
	/**
	 * Return the message data
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T data() throws ClassCastException
	{
		return (T)this.messageData;
	}
	
	/**
	 * Return the message data without implicit cast
	 * 
	 * @return
	 */
	public Object getRawMessageData()
	{
		return this.messageData;
	}
	
	/**
	 * Check whether the message data are of the specified type
	 * 
	 * @param thisClass Class to compare with
	 * @return
	 */
	public boolean dataInstanceOf(Class<?> thisClass)
	{
		if (thisClass == null)
		{
			 return this.messageData == null;
		}
		
		return thisClass.isInstance(this.messageData);
	}
	
	/**
	 * Check whether this message has the specified shortcode, more readable shorthand for
	 *    message.shortcode.equals(shortCode)
	 * 
	 * @param shortCode
	 * @return
	 */
	public boolean hasShortCode(String shortCode)
	{
		if (shortCode == null)
		{
			return false;
		}
		
		return shortCode.equals(this.shortCode);
	}
	
	/**
	 * Returns the index into the supplied argument list of the message's shortcode
	 * 
	 * @param shortCodes
	 * @return
	 */
	public int ordinal(String... shortCodes)
	{
		int ordinal = 0;
		
		for (String shortCode : shortCodes)
		{
			if (shortCode != null && shortCode.equals(this.shortCode))
				return ordinal;
			
			ordinal++;
		}
		
		return -1;
	}

	public boolean isLocal()
	{
		return this.isLocal;
	}

	public String getShortCode()
	{
		return this.shortCode;
	}

	public int getMessageId()
	{
		return this.messageId;
	}

	public int getMessageLength()
	{
		return this.messageLength;
	}

	public Player getSender()
	{
		return this.player;
	}
	
	public String getSenderName()
	{
		return this.player != null ? this.player.getName() : "Server";
	}

	public Entity getRelevantEntity()
	{
		return this.relevantEntity;
	}
}
