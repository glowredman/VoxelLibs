package com.thevoxelbox.voxelpacket.server;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.thevoxelbox.voxelpacket.common.VoxelPacket;

class VoxelPacketServerProxy implements Listener, PluginMessageListener 
{
	/**
	 * Log instance
	 */
	private static Logger log = Logger.getLogger("VoxelPacket");
	
	/**
	 * Server instance
	 */
	private VoxelPacketServer server;
	
	private Plugin hostPlugin;
	
	VoxelPacketServerProxy(VoxelPacketServer server)
	{
		this.server = server;
	}
	
	public void enable(Plugin hostPlugin)
	{
		this.hostPlugin = hostPlugin;

		Bukkit.getMessenger().registerOutgoingPluginChannel(this.hostPlugin, VoxelPacket.VOXELPACKET_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this.hostPlugin, VoxelPacket.VOXELPACKET_CHANNEL, this);

		Bukkit.getServer().getPluginManager().registerEvents(this, this.hostPlugin);
		
		log.info("[VoxelModPackPlugin] VoxelPacket subsystem ENABLED");
	}

	public void disable()
	{
		if (this.server != null)
		{
			log.info("[VoxelPacket] Shutting down VoxelPacket subsystem...");
			
			// Shut down the packet handler
//			this.server.shutdown();
			this.server = null;
		}
		
		log.info("[VoxelPacket] VoxelPacket subsystem DISABLED");
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.messaging.PluginMessageListener#onPluginMessageReceived(java.lang.String, org.bukkit.entity.Player, byte[])
	 */
	@Override
	public void onPluginMessageReceived(String channel, Player sender, byte[] data)
	{
		VoxelPacket packet = new VoxelPacket(channel, data.length, data);
		packet.handle(sender);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRegisterChannel(PlayerRegisterChannelEvent event)
	{
		if (this.server != null && VoxelPacket.VOXELPACKET_CHANNEL.equals(event.getChannel()))
		{
			this.server.queryPlayer(event.getPlayer(), "SUBS");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (this.server != null)
		{
			this.server.onPlayerQuit(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event)
	{
		if (this.server != null)
		{
			this.server.onPlayerQuit(event.getPlayer());
		}
	}

	public void sendPluginMessage(Player player, String channel, byte[] payload)
	{
		if (this.hostPlugin != null)
		{
			if (player instanceof CraftPlayer) ((CraftPlayer)player).addChannel(channel);
			player.sendPluginMessage(this.hostPlugin, channel, payload);
		}
	}
}
