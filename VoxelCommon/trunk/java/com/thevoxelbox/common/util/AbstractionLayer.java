package com.thevoxelbox.common.util;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

/**
 * This is a basic abstraction layer class for some of the more common Minecraft functions and members
 * 
 * @author Adam Mummery-Smith
 */
public final class AbstractionLayer
{
	/**
	 * Wrapper for ModLoader function
	 * 
	 * @return Minecraft instance
	 */
	public static Minecraft getMinecraft()
	{
		return Minecraft.getMinecraft();
	}
	
	/**
	 * Get the font renderer
	 */
	public static FontRenderer getFontRenderer()
	{
		return AbstractionLayer.getMinecraft().fontRenderer;
	}
	
	/**
	 * Get the render engine
	 */
	public static TextureManager getTextureManager()
	{
		return AbstractionLayer.getMinecraft().getTextureManager();
	}
	
	/**
	 * Get the game settings
	 */
	public static GameSettings getGameSettings()
	{
		return AbstractionLayer.getMinecraft().gameSettings;
	}
	
	/**
	 * Bind a texture in the render engine
	 * 
	 * @param textureName
	 */
	public static void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}
	
	/**
	 * Bind a texture in the render engine
	 * 
	 * @param textureName
	 */
	public static void bindTexture(int glTextureName)
	{
		glBindTexture(GL_TEXTURE_2D, glTextureName);
	}
	
	/**
	 * Check whether the current world is a multiplayer world
	 */
	public static boolean isMultiplayerWorld()
	{
		return !AbstractionLayer.getMinecraft().isSingleplayer();
	}
	
	/**
	 * Add a chat message to the ingame gui
	 * 
	 * @param message Message to add
	 */
	public static void addChatMessage(String message)
	{
		AbstractionLayer.getMinecraft().ingameGUI.getChatGUI().func_146227_a(new ChatComponentText(message)); // printChatMessage
	}
	
	/**
	 * Display a gui screen
	 * 
	 * @param guiscreen Screen to display or null to display no screen
	 */
	public static void displayGuiScreen(GuiScreen guiscreen)
	{
		AbstractionLayer.getMinecraft().displayGuiScreen(guiscreen);
	}
	
	/**
	 * Get the tessellator instance
	 */
	public static Tessellator getTessellator()
	{
		return Tessellator.instance;
	}
	
	/**
	 * Get the player entity
	 * 
	 * @return Current player entity
	 */
	public static EntityClientPlayerMP getPlayer()
	{
		return AbstractionLayer.getMinecraft().thePlayer;
	}
	
	public static EntityPlayer getPlayerMP()
	{
		Minecraft mc = AbstractionLayer.getMinecraft();
		IntegratedServer server = mc.getIntegratedServer();
		
		if (server != null)
		{
			return server.getConfigurationManager().getPlayerForUsername(server.getServerOwner());
		}
		
		return AbstractionLayer.getPlayer();
	}
	
	public static PlayerControllerMP getPlayerController()
	{
		return AbstractionLayer.getMinecraft().playerController;
	}
	
	public static WorldClient getWorld()
	{
		return AbstractionLayer.getMinecraft().theWorld;
	}
	
	public static Profiler getProfiler()
	{
		return AbstractionLayer.getMinecraft().mcProfiler;
	}
	
	/**
	 * Get the ingame gui
	 */
	 public static GuiIngame getIngameGui()
	 {
		 return AbstractionLayer.getMinecraft().ingameGUI;
	 }
}
