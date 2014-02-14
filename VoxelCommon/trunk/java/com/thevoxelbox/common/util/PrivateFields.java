package com.thevoxelbox.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import paulscode.sound.SoundSystem;

/**
 * Wrapper for obf/mcp reflection-accessed private fields, mainly added to centralise the locations I have to update the obfuscated field names
 * 
 * @author Adam Mummery-Smith
 *
 * @param <P> Parent class type, the type of the class that owns the field
 * @param <T> Field type, the type of the field value
 */
@SuppressWarnings("rawtypes")
public class PrivateFields<P, T>
{
	/**
	 * Class to which this field belongs
	 */
	public final Class<P> parentClass;
	
	/**
	 * Name used to access the field, determined at init
	 */
	private final String fieldName;

	private boolean errorReported;
	
	/**
	 * Creates a new private field entry
	 * 
	 * @param owner
	 * @param mcpName
	 * @param name
	 */
	private PrivateFields(Class<P> owner, ObfuscationMapping mapping)
	{
		this.parentClass = owner;
		this.fieldName = mapping.getName();
	}
	
	/**
	 * Get the current value of this field on the instance class supplied
	 * 
	 * @param instance Class to get the value of
	 * @return field value or null if errors occur
	 */
	@SuppressWarnings("unchecked")
	public T get(P instance)
	{
		try
		{
			return (T)Reflection.getPrivateValue(this.parentClass, instance, this.fieldName);
		}
		catch (Exception ex)
		{
			if (!this.errorReported)
			{
				this.errorReported = true;
				ex.printStackTrace();
			}
			return null;
		}
	}
	
	/**
	 * Set the value of this field on the instance class supplied
	 * 
	 * @param instance Object to set the value of the field on
	 * @param value value to set
	 * @return value
	 */
	public T set(P instance, T value)
	{
		try
		{
			Reflection.setPrivateValue(this.parentClass, instance, this.fieldName, value);
		}
		catch (Exception ex)
		{
			if (!this.errorReported)
			{
				this.errorReported = true;
				ex.printStackTrace();
			}
		}
		
		return value;
	}
	
	/**
	 * Static private fields
	 *
	 * @param <P> Parent class type, the type of the class that owns the field
	 * @param <T> Field type, the type of the field value
	 */
	public static final class StaticFields<P, T> extends PrivateFields<P, T>
	{
		@SuppressWarnings("synthetic-access")
		public StaticFields(Class<P> owner, ObfuscationMapping mapping) { super(owner, mapping); }
		public T get() { return this.get(null); }
		public void set(T value) { this.set(null, value); }
		
		public static final StaticFields<I18n, Locale>                            locale = new StaticFields<I18n, Locale>         (I18n.class,        ObfuscationMapping.currentLocale);
		public static final StaticFields<TileEntity, Map>       tileEntityNameToClassMap = new StaticFields<TileEntity, Map>      (TileEntity.class,  ObfuscationMapping.tileEntityNameToClassMap);
		public static final StaticFields<GuiMainMenu, Boolean>              realmsButton = new StaticFields<GuiMainMenu, Boolean> (GuiMainMenu.class, ObfuscationMapping.realmsButton);
		public static final StaticFields<Gui, ResourceLocation>        optionsBackground = new StaticFields<Gui, ResourceLocation>(Gui.class,         ObfuscationMapping.optionsBackground);
		public static final StaticFields<Blocks, Block>                    standing_sign = new StaticFields<Blocks, Block>        (Blocks.class,      ObfuscationMapping.standing_sign);
		public static final StaticFields<Blocks, Block>                        wall_sign = new StaticFields<Blocks, Block>        (Blocks.class,      ObfuscationMapping.wall_sign);
	}

	// If anyone screws up the formatting of this table again I will have them fed to a shark
	public static final PrivateFields<Minecraft, IntegratedServer>                              theIntegratedServer = new PrivateFields<Minecraft, IntegratedServer>                    (Minecraft.class,                            ObfuscationMapping.theIntegratedServer);
	public static final PrivateFields<Minecraft, Boolean>                                 integratedServerIsRunning = new PrivateFields<Minecraft, Boolean>                             (Minecraft.class,                            ObfuscationMapping.integratedServerIsRunning);
	public static final PrivateFields<Minecraft, NetworkManager>                                   myNetworkManager = new PrivateFields<Minecraft, NetworkManager>                     (Minecraft.class,                            ObfuscationMapping.myNetworkManager);
	public static final PrivateFields<GuiScreen, GuiButton>                                 guiScreenSelectedButton = new PrivateFields<GuiScreen, GuiButton>                           (GuiScreen.class,                            ObfuscationMapping.guiScreenSelectedButton);
	public static final PrivateFields<RenderGlobal, WorldRenderer[]>                                 worldRenderers = new PrivateFields<RenderGlobal, WorldRenderer[]>                  (RenderGlobal.class,                         ObfuscationMapping.worldRenderers);
	public static final PrivateFields<WorldInfo, WorldType>                                               worldType = new PrivateFields<WorldInfo, WorldType>                           (WorldInfo.class,                            ObfuscationMapping.worldType);
	public static final PrivateFields<GuiTextField, Integer>                                          textFieldXPos = new PrivateFields<GuiTextField, Integer>                          (GuiTextField.class,                         ObfuscationMapping.textFieldXPos);
	public static final PrivateFields<GuiTextField, Integer>                                          textFieldYPos = new PrivateFields<GuiTextField, Integer>                          (GuiTextField.class,                         ObfuscationMapping.textFieldYPos);
	public static final PrivateFields<GuiTextField, Integer>                                         textFieldWidth = new PrivateFields<GuiTextField, Integer>                          (GuiTextField.class,                         ObfuscationMapping.textFieldWidth);
	public static final PrivateFields<GuiTextField, Integer>                                        textFieldHeight = new PrivateFields<GuiTextField, Integer>                          (GuiTextField.class,                         ObfuscationMapping.textFieldHeight);
	public static final PrivateFields<FontRenderer, Float>                                         fontRendererPosY = new PrivateFields<FontRenderer, Float>                            (FontRenderer.class,                         ObfuscationMapping.fontRendererPosY);
	public static final PrivateFields<GuiSelectWorld, Boolean>                                        worldSelected = new PrivateFields<GuiSelectWorld, Boolean>                        (GuiSelectWorld.class,                       ObfuscationMapping.worldSelected);
	public static final PrivateFields<GuiSelectWorld, GuiScreen>                               guiSelectWorldParent = new PrivateFields<GuiSelectWorld, GuiScreen>                      (GuiSelectWorld.class,                       ObfuscationMapping.guiSelectWorldParent);
	public static final PrivateFields<RenderPlayer, ModelBiped>                                      modelBipedMain = new PrivateFields<RenderPlayer, ModelBiped>                       (RenderPlayer.class,                         ObfuscationMapping.modelBipedMain);
	public static final PrivateFields<RenderPlayer, ModelBiped>                                modelArmorChestplate = new PrivateFields<RenderPlayer, ModelBiped>                       (RenderPlayer.class,                         ObfuscationMapping.modelArmorChestplate);
	public static final PrivateFields<RenderPlayer, ModelBiped>                                          modelArmor = new PrivateFields<RenderPlayer, ModelBiped>                       (RenderPlayer.class,                         ObfuscationMapping.modelArmor);
	public static final PrivateFields<TileEntityMobSpawner, MobSpawnerBaseLogic>                       spawnerLogic = new PrivateFields<TileEntityMobSpawner, MobSpawnerBaseLogic>      (TileEntityMobSpawner.class,                 ObfuscationMapping.spawnerLogic);
	public static final PrivateFields<TileEntityRendererDispatcher, Map>                         specialRendererMap = new PrivateFields<TileEntityRendererDispatcher, Map>              (TileEntityRendererDispatcher.class,         ObfuscationMapping.specialRendererMap);
	public static final PrivateFields<SoundManager, SoundSystem>                                        soundSystem = new PrivateFields<SoundManager, SoundSystem>                      (SoundManager.class,                         ObfuscationMapping.soundSystemThread);
	public static final PrivateFields<GuiSlot, Long>                                                    lastClicked = new PrivateFields<GuiSlot, Long>                                  (GuiSlot.class,                              ObfuscationMapping.lastClicked);
	public static final PrivateFields<Minecraft, ServerData>                                      currentServerData = new PrivateFields<Minecraft, ServerData>                          (Minecraft.class,                            ObfuscationMapping.currentServerData);
	public static final PrivateFields<Minecraft, Timer>                                              minecraftTimer = new PrivateFields<Minecraft, Timer>                               (Minecraft.class,                            ObfuscationMapping.minecraftTimer);
	public static final PrivateFields<EntityRenderer, Double>                                            renderZoom = new PrivateFields<EntityRenderer, Double>                         (EntityRenderer.class,                       ObfuscationMapping.renderZoom);
	public static final PrivateFields<EntityRenderer, Double>                                          renderOfsetX = new PrivateFields<EntityRenderer, Double>                         (EntityRenderer.class,                       ObfuscationMapping.renderOfsetX);
	public static final PrivateFields<EntityRenderer, Double>                                          renderOfsetY = new PrivateFields<EntityRenderer, Double>                         (EntityRenderer.class,                       ObfuscationMapping.renderOfsetY);
	public static final PrivateFields<AbstractClientPlayer, ThreadDownloadImageData>                    skinTexture = new PrivateFields<AbstractClientPlayer, ThreadDownloadImageData>  (AbstractClientPlayer.class,                 ObfuscationMapping.skinTexture);
	public static final PrivateFields<AbstractClientPlayer, ThreadDownloadImageData>                   cloakTexture = new PrivateFields<AbstractClientPlayer, ThreadDownloadImageData>  (AbstractClientPlayer.class,                 ObfuscationMapping.cloakTexture);
	public static final PrivateFields<AbstractClientPlayer, ResourceLocation>                          skinResource = new PrivateFields<AbstractClientPlayer, ResourceLocation>         (AbstractClientPlayer.class,                 ObfuscationMapping.skinResource);
	public static final PrivateFields<AbstractClientPlayer, ResourceLocation>                         cloakResource = new PrivateFields<AbstractClientPlayer, ResourceLocation>         (AbstractClientPlayer.class,                 ObfuscationMapping.cloakResource);
	public static final PrivateFields<GuiMainMenu, ResourceLocation>                                panoramaTexture = new PrivateFields<GuiMainMenu, ResourceLocation>                  (GuiMainMenu.class,                          ObfuscationMapping.panoramaTexture);
	public static final PrivateFields<World, Float>                                                 rainingStrength = new PrivateFields<World, Float>                                   (World.class,                                ObfuscationMapping.rainingStrength);
	public static final PrivateFields<World, Float>                                              thunderingStrength = new PrivateFields<World, Float>                                   (World.class,                                ObfuscationMapping.thunderingStrength);
	public static final PrivateFields<ThreadDownloadImageData, BufferedImage>                       downloadedImage = new PrivateFields<ThreadDownloadImageData, BufferedImage>         (ThreadDownloadImageData.class,              ObfuscationMapping.downloadedImage);
	public static final PrivateFields<GuiMultiplayer, ServerList>                                internetServerList = new PrivateFields<GuiMultiplayer, ServerList>                     (GuiMultiplayer.class,                       ObfuscationMapping.internetServerList);
	public static final PrivateFields<GuiMultiplayer, ServerSelectionList>                      serverSelectionList = new PrivateFields<GuiMultiplayer, ServerSelectionList>            (GuiMultiplayer.class,                       ObfuscationMapping.serverSelectionList);
	public static final PrivateFields<GuiScreenResourcePacks, GuiScreen>               guiResourcePacksParentScreen = new PrivateFields<GuiScreenResourcePacks, GuiScreen>              (GuiScreenResourcePacks.class,               ObfuscationMapping.guiResourcePacksParentScreen);
	public static final PrivateFields<AbstractResourcePack, File>                          abstractResourcePackFile = new PrivateFields<AbstractResourcePack, File>                     (AbstractResourcePack.class,                 ObfuscationMapping.abstractResourcePackFile);
	public static final PrivateFields<Minecraft, Framebuffer>                                         mcFramebuffer = new PrivateFields<Minecraft, Framebuffer>                         (Minecraft.class,                            ObfuscationMapping.mcFramebuffer);

	public static final PrivateFields<SoundEventAccessorComposite, List<ISoundEventAccessor>>                   eventSounds = new PrivateFields<SoundEventAccessorComposite, List<ISoundEventAccessor>>         (SoundEventAccessorComposite.class, ObfuscationMapping.eventSounds);
	public static final PrivateFields<TextureManager, Map<ResourceLocation, ? extends ITextureObject>> resourceToTextureMap = new PrivateFields<TextureManager, Map<ResourceLocation, ? extends ITextureObject>>(TextureManager.class, ObfuscationMapping.resourceToTextureMap);
}

