package com.thevoxelbox.voxelpacket.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;

import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderCoord3D;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderEntityNBT;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderFloat;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderInt;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderIntArray;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderNull;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderObject;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderRaw;
import com.thevoxelbox.voxelpacket.common.encoders.VoxelPacketEncoderString;
import com.thevoxelbox.voxelpacket.common.interfaces.IPacketSenderDelegate;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelMessagePublisher;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelMessageSubscriber;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketHandler;
import com.thevoxelbox.voxelpacket.exceptions.InvalidPacketDataException;
import com.thevoxelbox.voxelpacket.exceptions.InvalidShortcodeException;
import com.thevoxelbox.voxelpacket.exceptions.MessageOverflowException;
import com.thevoxelbox.voxelpacket.exceptions.MissingEncoderException;

/**
 * Base class for Voxel Packet Handlers which contains common functionality between the client and server
 * 
 * @author Adam Mummery-Smith
 *
 */
public abstract class VoxelPacketHandlerBase implements IVoxelPacketHandler, IVoxelMessagePublisher
{
	// Constants for the fixed encoders
	protected static final int ENCODER_NULL         = -1; 
	protected static final int ENCODER_RAW          = 0; 
	protected static final int ENCODER_SERIALIZABLE = 1; 

	/**
	 * Packet data encoder modules
	 */
	protected static HashMap<Integer, IVoxelPacketEncoder<?>> encoders = new HashMap<Integer, IVoxelPacketEncoder<?>>();

	/**
	 * Null encoder for sending no payload
	 */
	protected static VoxelPacketEncoderNull nullEncoder = new VoxelPacketEncoderNull(ENCODER_NULL);
	
	/**
	 * Default fallback encoder in case all else fails
	 */
	protected static VoxelPacketEncoderRaw defaultEncoder = new VoxelPacketEncoderRaw(ENCODER_RAW);

	/**
	 * Default fallback encoder, encodes serializable objects that don't have a more specific encoder
	 */
	protected static VoxelPacketEncoderObject serializableEncoder = new VoxelPacketEncoderObject(ENCODER_SERIALIZABLE);

	/**
	 * Thread synchronisation object for the subscriber collection
	 */
	protected Object subscriberLock = new Object();
	
	/**
	 * Message subscibers
	 */
	protected HashMap<String, ArrayList<IVoxelMessageSubscriber>> subscriberMap = new HashMap<String, ArrayList<IVoxelMessageSubscriber>>();
	
	/**
	 * Thread synchronisation object for the message builder list
	 */
	protected Object messagesLock = new Object();
	
	/**
	 * Message builder list
	 */
	protected HashMap<Integer, VoxelMessage> messages = new HashMap<Integer, VoxelMessage>();
	
	/**
	 * Constructor
	 */
	protected VoxelPacketHandlerBase()
	{
        encoders.clear();
        
        this.registerEncoder(new VoxelPacketEncoderString(2));
        this.registerEncoder(new VoxelPacketEncoderInt(3));
        this.registerEncoder(new VoxelPacketEncoderFloat(4));
        this.registerEncoder(new VoxelPacketEncoderIntArray(5));
        this.registerEncoder(new VoxelPacketEncoderEntityNBT(6));
        this.registerEncoder(new VoxelPacketEncoderCoord3D(7));
        
        this.registerEncoder(defaultEncoder);
        this.registerEncoder(serializableEncoder);
	}

	/**
	 * Register a new encoder in this handler, the encoder must support a unique data type ID.
	 * 
	 * @param encoder Encoder to register
	 * @return true if the encoder was registered successfully, false if the requested data type id was already assigned
	 */
	@Override
	public boolean registerEncoder(IVoxelPacketEncoder<?> encoder)
	{
		Integer dataTypeId = Integer.valueOf(encoder.getDataTypeID());
	
		if (!encoders.containsKey(dataTypeId))
		{
			encoders.put(dataTypeId, encoder);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Send a new message, creates the required packets and then dispatches using the supplied delegate
	 * 
	 * @param packetSender Delegate for sending the generated packets
	 * @param shortCode Shortcode for the message
	 * @param messageData Message payload
	 * @param relevantEntity Relevant entity for the message
	 * 
	 * @throws MissingEncoderException Thrown if no matching encoder was found
	 * @throws InvalidPacketDataException 
	 * @throws InvalidShortcodeException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void sendMessage(IPacketSenderDelegate packetSender, String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		IVoxelPacketEncoder encoder = this.getEncoder(messageData);
		
		if (encoder != null)
		{
			int dataTypeId = encoder.getDataTypeID();
			byte[] payload = encoder.encode(messageData);

			C17PacketCustomPayload packet = VoxelPacket.createPayload(dataTypeId, relevantEntity, shortCode, payload);
			packetSender.sendPacket(packet);
		}
		else
		{
			throw new MissingEncoderException("Could not find an encoder for payload with type " + messageData.getClass().getName());
		}
	}
	
	/**
	 * Retrieve the current subscriber list for the specified shortcode
	 * 
	 * @param shortCode
	 * @return
	 */
	protected final ArrayList<IVoxelMessageSubscriber> getSubscribers(String shortCode)
	{
		synchronized (this.subscriberLock)
		{
			if (!this.subscriberMap.containsKey(shortCode))
			{
				this.subscriberMap.put(shortCode, new ArrayList<IVoxelMessageSubscriber>());
			}
			
			return this.subscriberMap.get(shortCode);
		}
	}
	
	/**
	 * Retrieve the union of the individual subscriber list for the specified shortcode and the global
	 * subscriber list and return them in a new, mutable arraylist
	 * 
	 * @param shortCode Shortcode to get subscribers for
	 * @return
	 */
	protected final ArrayList<IVoxelMessageSubscriber> getAllSubscribers(String shortCode)
	{
		// Current list of subscribers
		ArrayList<IVoxelMessageSubscriber> currentSubscribers = new ArrayList<IVoxelMessageSubscriber>();

		synchronized (this.subscriberLock)
		{
			// Create a "snapshot" of the current subscribers array to return.
			currentSubscribers.addAll(this.getSubscribers(shortCode));
			
			// Get any global subscribers, providing they weren't already subscribing directly to the shortcode
			ArrayList<IVoxelMessageSubscriber> globalSubscribers = this.getSubscribers("*");

			// Add global subscribers to the collection
			for (IVoxelMessageSubscriber globalSubscriber : globalSubscribers)
			{
				if (!currentSubscribers.contains(globalSubscriber))
					currentSubscribers.add(globalSubscriber);
			}
		}
		
		return currentSubscribers;
	}

	/**
	 * Purge all subscribers
	 */
	protected final void purgeSubscribers()
	{
		synchronized (this.subscriberLock)
		{
			for(Entry<String, ArrayList<IVoxelMessageSubscriber>> subscriberList : this.subscriberMap.entrySet())
			{
				subscriberList.getValue().clear();
			}
			
			this.subscriberMap.clear();
		}
		
		this.onPurgeSubscribers();
	}
	
	protected void onPurgeSubscribers()
	{
		// Stub
	}
	
	@Override
	public final IVoxelMessagePublisher subscribe(IVoxelMessageSubscriber subscriber, String shortCode)
	{
		synchronized (this.subscriberLock)
		{
			ArrayList<IVoxelMessageSubscriber> subscribers = this.getSubscribers(shortCode);
			
			if (!subscribers.contains(subscriber))
				subscribers.add(subscriber);
		}
		
		this.onSubscribe(subscriber, shortCode);
		
		return this;
	}
	
	protected void onSubscribe(IVoxelMessageSubscriber subscriber, String shortCode)
	{
		// Stub
	}

	@Override
	public final IVoxelMessagePublisher unsubscribe(IVoxelMessageSubscriber subscriber, String shortCode)
	{
		synchronized (this.subscriberLock)
		{
			if (this.subscriberMap.containsKey(shortCode))
			{
				ArrayList<IVoxelMessageSubscriber> subscribers = this.getSubscribers(shortCode);
				subscribers.remove(subscriber);
			}
		}
		
		this.onUnsubscribe(subscriber, shortCode);
		
		return this;
	}
	
	protected void onUnsubscribe(IVoxelMessageSubscriber subscriber, String shortCode)
	{
		// Stub
	}

	@Override
	public final void handleVoxelPacket(VoxelPacket packet)
	{
		if (packet != null) // && packet.isVoxelPacket)
		{
			// Dispatch happens outside of the message lock
			VoxelMessage dispatchMessage = null;
			
			// Box the packet message ID
			Integer messageId = Integer.valueOf(packet.packetMessageId);
			
			synchronized (this.messagesLock)
			{
				VoxelMessage msg = this.messages.get(messageId);
	
				try
				{
					// Message with this ID wasn't found, should be a new message
					if (msg == null)
					{
						msg = new VoxelMessage(this, packet);
					}
					else
					{
						throw new MessageOverflowException("Received message with an overlapping ID");
					}
					
					if (msg.isFinalised())
					{
						// Assign this here, we don't want to do it inside the message thread lock
						dispatchMessage = msg;
						
						// Remove the message from the assignment queue
						this.messages.remove(messageId);
					}
				}
				catch (MissingEncoderException ex)
				{
					ex.printStackTrace();
					this.messages.remove(messageId);
				}
				catch (MessageOverflowException ex)
				{
					ex.printStackTrace();
					this.messages.remove(messageId);
				}
			}
			
			// Dispatch a finalised message
			if (dispatchMessage != null)
			{
				ArrayList<IVoxelMessageSubscriber> currentSubscribers = this.getAllSubscribers(dispatchMessage.getShortCode());
				this.dispatchMessage(currentSubscribers, dispatchMessage);
			}
		}
	}
	
	/**
	 * Callback which subclasses must override to route the messages
	 * stop
	 * @param subscribers
	 * @param message
	 */
	protected abstract void dispatchMessage(ArrayList<IVoxelMessageSubscriber> subscribers, VoxelMessage message);
	
	@Override
	public final IVoxelPacketEncoder<?> getEncoder(int dataType)
	{
		if (dataType < 0)
		{
			return nullEncoder;
		}
		
		if (encoders.containsKey(Integer.valueOf(dataType)))
		{
			return encoders.get(Integer.valueOf(dataType));
		}
		
		return null;
	}

	@Override
	public final IVoxelPacketEncoder<?> getEncoder(Object dataObject)
	{
		if (dataObject == null)
		{
			return nullEncoder;
		}
		
		for (IVoxelPacketEncoder<?> encoder : encoders.values())
		{
			Class<?> encoderClass = this.getGenericSuperClass(encoder);
			
			if (encoderClass != null && encoderClass.equals(dataObject.getClass()))
				return encoder;
		}

		for (IVoxelPacketEncoder<?> encoder : encoders.values())
		{
			if (this.getGenericSuperClassName(encoder).equals(dataObject.getClass().getSimpleName()))
				return encoder;
		}
		
		return serializableEncoder;
	}

	/**
	 * Attempts to determine the generic type of the specified encoder 
	 * 
	 * @param encoder
	 * @return
	 */
	protected final Class<?> getGenericSuperClass(IVoxelPacketEncoder<?> encoder)
	{
		try
		{
			Type encoderType = encoder.getClass().getGenericInterfaces()[0];
			
			if (encoderType instanceof ParameterizedType)
			{
				ParameterizedType parameterizedEncoderType = (ParameterizedType)encoderType;
				Type encoderTypeType = parameterizedEncoderType.getActualTypeArguments()[0];
				
				if (encoderTypeType instanceof GenericArrayTypeImpl)
					return null;
	
				return (Class<?>)encoderType;
			}
		}
		catch (Exception ex) {}
		
		return null;
	}
	
	/**
	 * Generates the generic type name of the specified encoder, useful for array types because the class discovery will fail
	 * 
	 * @param encoder
	 * @return
	 */
	protected final String getGenericSuperClassName(IVoxelPacketEncoder<?> encoder)
	{
		Type encoderType = encoder.getClass().getGenericInterfaces()[0];
		
		if (encoderType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedEncoderType = (ParameterizedType)encoderType;
			Type encoderTypeType = parameterizedEncoderType.getActualTypeArguments()[0];
			
			if (encoderTypeType instanceof GenericArrayTypeImpl)
			{
				GenericArrayTypeImpl encoderArrayType = (GenericArrayTypeImpl)encoderTypeType;
				encoderTypeType = encoderArrayType.getGenericComponentType();
				return ((Class<?>)encoderTypeType).getSimpleName() + "[]"; 		
			}

			return ((Class<?>)encoderTypeType).getSimpleName();
		}
		
		return ((Class<?>)encoderType).getSimpleName();
	}
}
