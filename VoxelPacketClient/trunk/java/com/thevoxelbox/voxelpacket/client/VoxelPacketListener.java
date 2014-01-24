package com.thevoxelbox.voxelpacket.client;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;

import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.PluginChannelListener;
import com.thevoxelbox.voxelpacket.common.VoxelPacket;
import com.thevoxelbox.voxelpacket.common.interfaces.IChatHandler;

public class VoxelPacketListener implements PluginChannelListener, ChatFilter
{
	private static IChatHandler chatHandler; 
	
	public VoxelPacketListener()
	{
	}
	
	@Override
	public void onJoinGame(INetHandler netHandler, S01PacketJoinGame joinGamePacket)
	{
	}
	
	@Override
	public String getName()
	{
		return "VoxelPacket";
	}
	
	@Override
	public String getVersion()
	{
		return "2.1.2";
	}
	
	@Override
	public void init(File configPath)
	{
		VoxelPacketClient.getInstance();
	}
	
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath)
	{
	}
	
	@Override
	public List<String> getChannels()
	{
		return Arrays.asList(new String[] { VoxelPacket.VOXELPACKET_CHANNEL });
	}
	
	@Override
	public void onCustomPayload(String channel, int length, byte[] data)
	{
		try
		{
			@SuppressWarnings("unused")
			VoxelPacket packet = new VoxelPacket(channel, length, data);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void registerPacketHandler(IChatHandler handler)
	{
		VoxelPacketListener.chatHandler = handler;
	}
	
	@Override
	public boolean onChat(S02PacketChat chatPacket, IChatComponent chat, String message)
	{
		if (VoxelPacketListener.chatHandler != null)
		{
			return !VoxelPacketListener.chatHandler.handleChatPacket(chatPacket, message);
		}
		
		return true;
	}
}
