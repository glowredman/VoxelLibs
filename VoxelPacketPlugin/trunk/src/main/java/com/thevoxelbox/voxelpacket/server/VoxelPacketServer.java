package com.thevoxelbox.voxelpacket.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.thevoxelbox.voxelpacket.common.Util;
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

public class VoxelPacketServer extends VoxelPacketHandlerBase implements IVoxelMessageSubscriber
{
	private static final String VERSION = "2.2.0";
	
	private static final String QUERY_SHORTCODE = "@INFO";

	/**
	 * Using this we can support singleton subclassing provided the external mod sets this value
	 * before GetInstance() is called for the first time
	 */
	public static Class<? extends VoxelPacketServer> instanceClass = VoxelPacketServer.class;
	
	/**
	 * Singleton instance
	 */
	protected static VoxelPacketServer instance;
	
	/**
	 * Log writer
	 */
	protected Logger log = Logger.getLogger("VoxelPacket");
	
	/**
	 * CraftBukkit server instance
	 */
	protected Server server;
	
	/**
	 * Delegates for individual players
	 */
	protected HashMap<String, IPacketSenderDelegate> playerDelegates = new HashMap<String, IPacketSenderDelegate>();
	
	/**
	 * Subscriber information from query - not necessarily 100% reliable but a good guess for unimportant messages
	 */
	private HashMap<String, ArrayList<String>> clientSubscriberMap = new HashMap<String, ArrayList<String>>();
	
	private VoxelPacketServerProxy bukkitProxy;
	
	/**
	 * .ctor, private per singleton pattern
	 */
	protected VoxelPacketServer()
	{
        VoxelPacket.registerPacketHandler(this);
		
		this.server = Util.getServer();
		this.clientSubscriberMap.clear();
		this.bukkitProxy = new VoxelPacketServerProxy(this);

		// Subscribe to query responses from clients 
		this.subscribe(this, VoxelPacketServer.QUERY_SHORTCODE);
	}
	
	/**
	 * Fire ze missiles
	 */
	public void shutdown()
	{
		this.purgeSubscribers();
		VoxelPacket.registerPacketHandler(null);
		instance.bukkitProxy.disable();
		instance.server = null;
		instance = null;
	}
	
	/**
	 * Get the version string for this release, you're welcome Mike
	 */
	public static String getVersion()
	{
		return VoxelPacketServer.VERSION;
	}

	/**
	 * Get the singleton instance
	 *  
	 * @return
	 */
	public static VoxelPacketServer getInstance()
	{
		if (instance == null)
		{
			throw new IllegalStateException("VoxelPacketServer.getInstance() called before initInstance()");
		}
		
		return instance;
	}
	
	/**
	 * Initialse the singleton instance
	 * 
	 * @param plugin
	 * @return
	 */
	public static VoxelPacketServer initInstance(Plugin plugin)
	{
		if (instance == null)
		{
			if (plugin == null)
			{
				throw new IllegalArgumentException("VoxelPacketServer.initInstance() called with null plugin reference");
			}

			try
			{
				// Support singleton subclassing through reflection
				instance = instanceClass.newInstance();
			}
			catch (IllegalAccessException ex)
			{
				// Reflective instancing failed, make a default
				instance = new VoxelPacketServer();
			}
			catch (InstantiationException ex)
			{
				// Reflective instancing failed, make a default
				instance = new VoxelPacketServer();
			}
			
			if (instance != null)
			{
				instance.bukkitProxy.enable(plugin);
			}
		}
		
		return instance;
	}
	
	/**
	 * Get the list of reported subscriptions for the specified player
	 * 
	 * @param playerName Name of the player to look up
	 * @return
	 */
	protected ArrayList<String> getKnownSubsForPlayer(String playerName)
	{
		if (!this.clientSubscriberMap.containsKey(playerName))
			this.clientSubscriberMap.put(playerName, new ArrayList<String>());
		
		return this.clientSubscriberMap.get(playerName);
	}

	/**
	 * Add a known subscription for the specified player 
	 * 
	 * @param playerName
	 * @param sub
	 */
	protected void addKnownSubForPlayer(String playerName, String sub)
	{
		ArrayList<String> subs = this.getKnownSubsForPlayer(playerName);
		if (!subs.contains(sub)) subs.add(sub);
	}
	
	@Override
	public void receiveMessage(IVoxelMessagePublisher publisher, VoxelMessage message)
	{
		if (message.hasShortCode(VoxelPacketServer.QUERY_SHORTCODE))
		{
			QueryResponse response = message.data();
			
			if (response != null)
			{
				if (response.responseCode.equals("SUBS"))
				{
					ArrayList<String> subs = response.data();
					this.clientSubscriberMap.put(message.getSenderName(), subs);
				}
				else if (response.responseCode.equals("SUB"))
				{
					String sub = response.data();
					this.addKnownSubForPlayer(message.getSenderName(), sub);
				}
			}
			else
			{
				this.log.warning("[VoxelPacket] Invalid query response from \"" + message.getSenderName() + "\", null payload");
			}
		}
	}
	
	@Override
	public void receiveMessageClassCastFailure(IVoxelMessagePublisher publisher, VoxelMessage message, ClassCastException ex)
	{
		this.log.warning("[VoxelPacket] Error processing query response from client " + message.getSenderName());
	}

	/**
	 * Send a query packet to the specified client
	 * 
	 * @param player
	 */
	public void queryPlayer(Player player, String queryType)
	{
		try
		{
			this.log.info("[VoxelPacket] Sending subscription query to client " + player.getName());
			this.sendMessageTo(player, "@QUERY", queryType, null);
		}
		catch (VoxelPacketException e) {}
	}
	
	/**
	 * A player left the server, remove any cached subscription information
	 * 
	 * @param player
	 */
	public void onPlayerQuit(Player player)
	{
		if (player != null)
		{
			this.clientSubscriberMap.remove(player.getName());
			this.playerDelegates.remove(player.getName());
		}
	}
	
	/**
	 * Sends the specified message to all connected clients
	 * 
	 * @param shortCode Shortcode of the message to send
	 * @param messageData Message payload
	 * @param relevantEntity Entity ID to include with this message
	 * 
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException
	 * @throws InvalidShortcodeException
	 */
	public void sendMessage(String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		IPacketSenderDelegate packetSender = this.getDelegateForAllPlayers();
		this.sendMessage(packetSender, shortCode, messageData, relevantEntity);
	}
	
	/**
	 * Sends the specified message to a single client 
	 * 
	 * @param player Client to send the message to
	 * @param shortCode Shortcode of the message to send
	 * @param messageData Message payload
	 * @param relevantEntity Entity ID to include with this message
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException
	 * @throws InvalidShortcodeException
	 */
	public void sendMessageTo(Player player, String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		IPacketSenderDelegate packetSender = this.getDelegateForPlayer(player);
		this.sendMessage(packetSender, shortCode, messageData, relevantEntity);
	}		

	/**
	 * Send the specified message to clients which we know are subscribed
	 * 
	 * @param shortCode
	 * @param messageData
	 * @param relevantEntity
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException
	 * @throws InvalidShortcodeException
	 */
	public void sendMessageToKnownSubscribers(String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		List<Player> players = this.getKnownSubscribers(shortCode);
		this.sendMessageToPlayers(players, shortCode, messageData, relevantEntity);
	}
	
	/**
	 * Sends the specified message to all clients in the list 
	 * 
	 * @param player Client to send the message to
	 * @param shortCode Shortcode of the message to send
	 * @param messageData Message payload
	 * @param relevantEntity Entity ID to include with this message
	 * @throws MissingEncoderException
	 * @throws InvalidPacketDataException
	 * @throws InvalidShortcodeException
	 */
	public void sendMessageToPlayers(List<Player> players, String shortCode, Object messageData, Entity relevantEntity) throws MissingEncoderException, InvalidPacketDataException, InvalidShortcodeException
	{
		if (players != null && players.size() > 0)
		{
			IPacketSenderDelegate packetSender = this.getDelegateForPlayers(players);
			this.sendMessage(packetSender, shortCode, messageData, relevantEntity);
		}
	}

	/**
	 * Callback used by the packet sender delegates to actually dispatch a packet to the player 
	 * 
	 * @param packet Packet to send
	 * @param player Player to send to
	 */
	protected void onSendPayloadToPlayer(Player player, String channel, byte[] payload)
	{
		try
		{
			if (player != null && payload != null)
			{
				this.bukkitProxy.sendPluginMessage(player, channel, payload);
			}
		}
		catch (Exception ex) { ex.printStackTrace(); } 
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
	
	/**
	 * Return a list of player entities who have reported that they are subscribed to the specified shortcode
	 * 
	 * @param shortCode
	 * @return
	 */
	protected List<Player> getKnownSubscribers(String shortCode)
	{
		List<Player> subscribingPlayers = new ArrayList<Player>();
		
		for (Entry<String, ArrayList<String>> subscriptionInfo : this.clientSubscriberMap.entrySet())
		{
			if (subscriptionInfo.getValue().contains(shortCode))
			{
				Player player = this.server.getPlayer(subscriptionInfo.getKey());
				subscribingPlayers.add(player);
			}
		}
		
		return subscribingPlayers;
	}
	
	/**
	 * Create a packet sender delegate for the specified player entity
	 * 
	 * @param player
	 * @return
	 */
	protected IPacketSenderDelegate getDelegateForPlayer(Player player)
	{
		if (!this.playerDelegates.containsKey(player.getName()))
		{
			this.playerDelegates.put(player.getName(), new PlayerDelegate(player));
		}
		
		return this.playerDelegates.get(player.getName());
	}
	
	/**
	 * Create a packet sender delegate for the supplied player list
	 * 
	 * @param players
	 * @return
	 */
	protected IPacketSenderDelegate getDelegateForPlayers(List<Player> players)
	{
		return new MultiplePlayerDelegate(players);
	}
	
	/**
	 * Create a packet sender delegate for all players on the server
	 * 
	 * @return
	 */
	protected IPacketSenderDelegate getDelegateForAllPlayers()
	{
		return new BroadcastDelegate();
	}
	
	/**
	 * 
	 * @author Adam Mummery-Smith
	 *
	 */
	class PlayerDelegate implements IPacketSenderDelegate
	{
		private final Player player;
		
		PlayerDelegate(Player player)
		{
			this.player = player;
		}
		
		@Override
		public void sendPayload(String channel, byte[] payload)
		{
			VoxelPacketServer.this.onSendPayloadToPlayer(this.player, channel, payload);
		}
	}

	/**
	 * 
	 * @author Adam Mummery-Smith
	 *
	 */
	class MultiplePlayerDelegate implements IPacketSenderDelegate
	{
		private final List<Player> players;
		
		MultiplePlayerDelegate(List<Player> players)
		{
			this.players = players;
		}
		
		@Override
		public void sendPayload(String channel, byte[] payload)
		{
			for (Player player : this.players)
				VoxelPacketServer.this.onSendPayloadToPlayer(player, channel, payload);
		}
	}
	
	/**
	 * 
	 * @author Adam Mummery-Smith
	 */
	class BroadcastDelegate implements IPacketSenderDelegate
	{
		private final List<Player> players;
		
		public BroadcastDelegate()
		{
			this.players = Arrays.asList(VoxelPacketServer.this.server.getOnlinePlayers());
		}
		
		@Override
		public void sendPayload(String channel, byte[] payload)
		{
			for (Player player : this.players)
				VoxelPacketServer.this.onSendPayloadToPlayer(player, channel, payload);
		}
	}
}
