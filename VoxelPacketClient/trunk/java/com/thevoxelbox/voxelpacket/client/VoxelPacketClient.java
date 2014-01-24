package com.thevoxelbox.voxelpacket.client;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

import com.thevoxelbox.voxelpacket.common.VoxelMessage;
import com.thevoxelbox.voxelpacket.common.VoxelPacket;
import com.thevoxelbox.voxelpacket.common.VoxelPacketHandlerBase;
import com.thevoxelbox.voxelpacket.common.interfaces.IPacketSenderDelegate;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelMessagePublisher;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelMessageSubscriber;
import com.thevoxelbox.voxelpacket.common.struct.QueryResponse;
import com.thevoxelbox.voxelpacket.exceptions.InvalidPacketDataException;
import com.thevoxelbox.voxelpacket.exceptions.InvalidShortcodeException;
import com.thevoxelbox.voxelpacket.exceptions.MissingEncoderException;
import com.thevoxelbox.voxelpacket.exceptions.VoxelPacketException;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketClient extends VoxelPacketHandlerBase implements IVoxelMessageSubscriber
{
	/**
	 * Using this we can support singleton subclassing provided the external mod sets this value
	 * before GetInstance() is called for the first time
	 */
	public static Class<? extends VoxelPacketClient> instanceClass = VoxelPacketClient.class;
	
	/**
	 * Singleton instance
	 */
	private static VoxelPacketClient instance;
	
	/**
	 * Minecraft instance
	 */
	protected Minecraft minecraft;
	
	/**
	 * Outbound packet delegate
	 */
	protected IPacketSenderDelegate senderDelegate;
	
	/**
	 * Chat manager, handles subscribers for 
	 */
	protected ChatManager chatManager;
	
	/**
	 * Protected constructor for singleton pattern
	 */
	protected VoxelPacketClient()
	{
		super();
		
		this.subscribe(this, "@QUERY");
		
        VoxelPacket.registerPacketHandler(this);

		this.minecraft = Minecraft.getMinecraft();
		this.chatManager = new ChatManager(this.minecraft);
		
		// Using a delegate allows us to keep SendPacket private
		this.senderDelegate = new IPacketSenderDelegate()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public void sendPacket(Packet packet)
			{
				VoxelPacketClient.this.onSendPacket(packet);
			}
		};
	}
	
	@Override
	protected void onPurgeSubscribers()
	{
		this.subscribe(this, "@QUERY");
	}

	/**
	 * Get the singleton instance
	 *  
	 * @return
	 */
	public static VoxelPacketClient getInstance()
	{
		if (instance == null)
		{
			try
			{
				// Support singleton subclassing through reflection
				instance = instanceClass.newInstance();
			}
			catch (IllegalAccessException ex)
			{
				// Reflective instancing failed, make a default
				instance = new VoxelPacketClient();
			}
			catch (InstantiationException ex)
			{
				// Reflective instancing failed, make a default
				instance = new VoxelPacketClient();
			}
		}
		
		return instance;
	}
	
	/**
	 * Access the chat manager instance
	 * 
	 * @return
	 */
	public ChatManager getChatManager()
	{
		return this.chatManager;
	}
	
	/**
	 * Send a message to local and remote subscribers, catch thrown exceptions and return false
	 * 
	 * @param shortCode
	 * @param messageData
	 * @return
	 */
	public boolean sendMessageEx(String shortCode, Object messageData)
	{
		try
		{
			this.sendMessage(shortCode, messageData, null, MessageRoutingScheme.Both);
			return true;
		}
		catch (VoxelPacketException ex) { return false; }
	}
	
	/**
	 * Send a message to local and remote subscribers
	 * 
	 * @param shortCode
	 * @param messageData
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException 
	 * @throws InvalidShortcodeException 
	 */
	public void sendMessage(String shortCode, Object messageData) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		this.sendMessage(shortCode, messageData, null, MessageRoutingScheme.Both);
	}
	
	/**
	 * Sends a message to local and remote subscribers with a relevant entity ID, catch thrown exceptions and return false
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param relevantEntity
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException
	 * @throws InvalidShortcodeException
	 */
	public boolean sendMessageEx(String shortCode, Object messageData, Entity relevantEntity)
	{
		try
		{
			this.sendMessage(shortCode, messageData, relevantEntity, MessageRoutingScheme.Both);
			return true;
		}
		catch (VoxelPacketException ex) { return false; }
	}
	
	/**
	 * Sends a message to local and remote subscribers with a relevant entity ID
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param relevantEntity
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException 
	 * @throws InvalidShortcodeException 
	 */
	public void sendMessage(String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		this.sendMessage(shortCode, messageData, relevantEntity, MessageRoutingScheme.Both);
	}
	
	/**
	 * Send a message to the subscriber subset denoted by the supplied routing scheme, catch thrown exceptions and return false
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param routingScheme
	 * @return
	 */
	public boolean sendMessageEx(String shortCode, Object messageData, MessageRoutingScheme routingScheme)
	{
		try
		{
			this.sendMessage(shortCode, messageData, null, routingScheme);
			return true;
		}
		catch (VoxelPacketException ex) { return false; }
	}
	
	/**
	 * Send a message to the subscriber subset denoted by the supplied routing scheme
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param routingScheme
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException 
	 * @throws InvalidShortcodeException 
	 */
	public void sendMessage(String shortCode, Object messageData, MessageRoutingScheme routingScheme) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		this.sendMessage(shortCode, messageData, null, routingScheme);
	}

	/**
	 * Sends the specified message based on the supplied routing scheme, catch thrown exceptions and return false
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param relevantEntity
	 * @param routingScheme
	 * @return
	 */
	public boolean sendMessageEx(String shortCode, Object messageData, Entity relevantEntity, MessageRoutingScheme routingScheme)
	{
		try
		{
			this.sendMessage(shortCode, messageData, relevantEntity, routingScheme);
			return true;
		}
		catch (VoxelPacketException ex) { return false; }
	}
	
	/**
	 * Sends the specified message based on the supplied routing scheme
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param relevantEntity
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException 
	 * @throws InvalidShortcodeException 
	 */
	public void sendMessage(String shortCode, Object messageData, Entity relevantEntity, MessageRoutingScheme routingScheme) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		if (routingScheme.routeLocally())
		{
			ArrayList<IVoxelMessageSubscriber> subscribers = this.getAllSubscribers(shortCode);
			
			if (subscribers.size() > 0)
				this.dispatchMessage(subscribers, new VoxelMessage(shortCode, messageData, relevantEntity));
		}
		
		if (routingScheme.routeRemotely())
		{
			this.sendMessage(this.senderDelegate, shortCode, messageData, relevantEntity);
		}
	}

	/**
	 * Callback from the sender delegate
	 * 
	 * @param packet Packet to send
	 */
	private void onSendPacket(Packet packet)
	{
		if (this.minecraft != null && this.minecraft.getNetHandler() != null)
		{
			this.minecraft.getNetHandler().addToSendQueue(packet);
		}
	}

	@Override
	protected void dispatchMessage(ArrayList<IVoxelMessageSubscriber> subscribers, VoxelMessage message)
	{
		for (IVoxelMessageSubscriber subscriber : subscribers)
		{
			try
			{
				subscriber.receiveMessage(this, message);
			}
			catch (ClassCastException ex)
			{
				subscriber.receiveMessageClassCastFailure(this, message, ex);
			}
			catch (Exception ex)
			{
				// Bad subscriber is bad
			}
		}
	}

	@Override
	public void receiveMessage(IVoxelMessagePublisher publisher, VoxelMessage message)
	{
		try
		{
			if (message.hasShortCode("@QUERY"))
			{
				String queryString = message.data();
				ArrayList<String> queryData = new ArrayList<String>();
				
				if (queryString.equals("SUBS"))
				{
					synchronized (this.subscriberLock)
					{
						queryData.addAll(this.subscriberMap.keySet());
					}
					
					queryData.remove("@QUERY");
					queryData.remove("*");
				}
				else
				{
					return;
				}
				
				this.sendMessage("@INFO", new QueryResponse(queryString, queryData), MessageRoutingScheme.Both);
			}
		}
		catch (Exception ex) { }
	}

	@Override
	public void receiveMessageClassCastFailure(IVoxelMessagePublisher publisher, VoxelMessage message, ClassCastException ex)
	{
		System.err.println("Error casting data from query packet " + message.data().toString());
	}
}
