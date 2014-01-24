package com.thevoxelbox.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import com.thevoxelbox.common.entity.EntityOtherPlayerMPHookable;
import com.thevoxelbox.common.interfaces.IEntityOtherPlayerMPSpawnListener;

/**
 * Main mod class for VoxelCommon
 * 
 * @author Adam Mummery-Smith
 */
public class LiteModVoxelCommon implements LiteMod
{
	/**
	 * 
	 */
	public static final String VERSION = "2.2.2";

	public static final ResourceLocation GUIPARTS = new ResourceLocation("voxelcommon", "textures/gui/guiparts.png");

	private static String NAME = "VoxelLib";
	
	private static Class<? extends EntityOtherPlayerMPHookable> hookableOtherPlayerClass = EntityOtherPlayerMPHookable.class;
	
	private static List<IEntityOtherPlayerMPSpawnListener> spawnListeners = new LinkedList<IEntityOtherPlayerMPSpawnListener>();
	
	/* (non-Javadoc)
	 * @see com.mumfrey.liteloader.LiteMod#getName()
	 */
	@Override
	public String getName()
	{
		return NAME;
	}
	
	/* (non-Javadoc)
	 * @see com.mumfrey.liteloader.LiteMod#getVersion()
	 */
	@Override
	public String getVersion()
	{
		return VERSION;
	}
	
	/* (non-Javadoc)
	 * @see com.mumfrey.liteloader.LiteMod#init(java.io.File)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(File configPath)
	{
		try
		{
			Class<? extends LiteMod> voxelPacketListenerClass = (Class<? extends LiteMod>)Class.forName("com.thevoxelbox.voxelpacket.client.VoxelPacketListener");
			LiteMod voxelPacketListener = voxelPacketListenerClass.newInstance();
			voxelPacketListener.init(configPath);
			LiteLoader.getEvents().addListener(voxelPacketListener);
			LiteModVoxelCommon.NAME += " + VoxelPacket";
		}
		catch (Throwable th)
		{
			th.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mumfrey.liteloader.LiteMod#upgradeSettings(java.lang.String, java.io.File, java.io.File)
	 */
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath)
	{
	}
	
	/**
	 * Allow the hookable class to be over-ridden 
	 * 
	 * @param otherPlayerMPClass
	 */
	public static void registerOtherPlayerMPClass(Class<? extends EntityOtherPlayerMPHookable> otherPlayerMPClass)
	{
		try
		{
			@SuppressWarnings("unchecked")
			Constructor<EntityOtherPlayerMPHookable> otherPlayerCtor = (Constructor<EntityOtherPlayerMPHookable>)hookableOtherPlayerClass.getDeclaredConstructor(World.class, String.class);
			if (otherPlayerCtor == null) return;
		}
		catch (Exception ex) { return; }
		
		hookableOtherPlayerClass = otherPlayerMPClass;
	}
	
	/**
	 * Register an listener that wants to be notified of player spawn events
	 * 
	 * @param listener
	 */
	public static void registerEntitySpawnListener(IEntityOtherPlayerMPSpawnListener listener)
	{
		if (!spawnListeners.contains(listener))
		{
			spawnListeners.add(listener);
		}
	}

	/**
	 * @param mc
	 * @param packet
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public static void processSpawnPlayer(INetHandler netHandler, S0CPacketSpawnPlayer packet)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		for (IEntityOtherPlayerMPSpawnListener listener : LiteModVoxelCommon.spawnListeners)
			listener.onPreSpawnEntity(packet);

		double xPos = (double)packet.func_148942_f() / 32.0D;
		double yPos = (double)packet.func_148949_g() / 32.0D;
		double zPos = (double)packet.func_148946_h() / 32.0D;
		float yaw = (float)(packet.func_148941_i() * 360) / 256.0F;
		float pitch = (float)(packet.func_148945_j() * 360) / 256.0F;
		EntityOtherPlayerMPHookable otherPlayer = LiteModVoxelCommon.spawnOtherPlayerMP(mc, packet);
		otherPlayer.prevPosX = otherPlayer.lastTickPosX = (double)(otherPlayer.serverPosX = packet.func_148942_f());
		otherPlayer.prevPosY = otherPlayer.lastTickPosY = (double)(otherPlayer.serverPosY = packet.func_148949_g());
		otherPlayer.prevPosZ = otherPlayer.lastTickPosZ = (double)(otherPlayer.serverPosZ = packet.func_148946_h());
		int selectedItem = packet.func_148947_k();
		
		if (selectedItem == 0)
		{
			otherPlayer.inventory.mainInventory[otherPlayer.inventory.currentItem] = null;
		}
		else
		{
			otherPlayer.inventory.mainInventory[otherPlayer.inventory.currentItem] = new ItemStack(Item.getItemById(selectedItem), 1, 0);
		}
		
		otherPlayer.setPositionAndRotation(xPos, yPos, zPos, yaw, pitch);
		mc.theWorld.addEntityToWorld(packet.func_148943_d(), otherPlayer);
		List<DataWatcher.WatchableObject> watchedMetaData = packet.func_148944_c(); // getWatchedMetadata
		
		if (watchedMetaData != null)
		{
			otherPlayer.getDataWatcher().updateWatchedObjectsFromList(watchedMetaData);
		}

		for (IEntityOtherPlayerMPSpawnListener listener : LiteModVoxelCommon.spawnListeners)
			listener.onPostSpawnEntity(packet, otherPlayer);
	}

	/**
	 * @param mc
	 * @param packet
	 * @return
	 */
	protected static EntityOtherPlayerMPHookable spawnOtherPlayerMP(Minecraft mc, S0CPacketSpawnPlayer packet)
	{
		try
		{
			@SuppressWarnings("unchecked")
			Constructor<EntityOtherPlayerMPHookable> otherPlayerCtor = (Constructor<EntityOtherPlayerMPHookable>)hookableOtherPlayerClass.getDeclaredConstructor(World.class, GameProfile.class);
			
			if (otherPlayerCtor != null)
			{
				return otherPlayerCtor.newInstance(mc.theWorld, packet.func_148948_e()); // getProfile
			}
		}
		catch (Exception ex) { ex.printStackTrace(); }
		
		return new EntityOtherPlayerMPHookable(mc.theWorld, packet.func_148948_e()); // getProfile
	}
}
