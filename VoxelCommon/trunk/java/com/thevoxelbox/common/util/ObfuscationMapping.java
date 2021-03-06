package com.thevoxelbox.common.util;

import com.mumfrey.liteloader.util.ModUtilities;

/**
 * Central list of obfuscation mappings used throughout macros, coalesced here instead of being spread throughout
 * the different reflection mechanisms 
 * 
 * @author Adam Mummery-Smith
 *
 * TODO Obfuscation - updated 1.7.2
 */
public enum ObfuscationMapping
{
              minecraftTimer("timer",                     "Q",  "field_71428_T"),  // Minecraft/timer
                    debugFPS("debugFPS",                  "ac", "field_71470_ab"), // Minecraft/debugFPS
           currentServerData("currentServerData",         "K",  "field_71422_O"),  // Minecraft/currentServerData
           shapedRecipeWidth("recipeWidth",               "a",  "field_77576_b"),  // ShapedRecipes/recipeWidth
          shapedRecipeHeight("recipeHeight",              "b",  "field_77577_c"),  // ShapedRecipes/recipeHeight
           shapedRecipeItems("recipeItems",               "c",  "field_77574_d"),  // ShapedRecipes/recipeItems
        shapelessRecipeItems("recipeItems",               "b",  "field_77579_b"),  // ShapelessRecipes/recipeItems
               textFieldXPos("xPosition",                 "f",  "field_146209_f"), // GuiTextField/xPos
               textFieldYPos("yPosition",                 "g",  "field_146210_g"), // GuiTextField/yPos
              textFieldWidth("width",                     "h",  "field_146218_h"), // GuiTextField/width
             textFieldHeight("height",                    "i",  "field_146219_i"), // GuiTextField/height
          textFieldIsEnabled("isEnabled",                 "p",  "field_146226_p"), // GuiTextField/isEnabled
          textFieldScrollPos("lineScrollOffset",          "q",  "field_146225_q"), // GuiTextField/lineScrollOffset
     textFieldSelectionStart("cursorPosition",            "r",  "field_146224_r"), // GuiTextField/cursorPosition
       textFieldSelectionEnd("selectionEnd",              "s",  "field_146223_s"), // GuiTextField/selectionEnd
      textFieldEnabledColour("enabledColor",              "t",  "field_146222_t"), // GuiTextField/enabledColor
     textFieldDisabledColour("disabledColor",             "u",  "field_146221_u"), // GuiTextField/disabledColor
             creativeBinSlot( null,                       "C",  "field_147064_C"), // GuiContainerCreative/field_147064_C
           creativeGuiScroll("currentScroll",             "x",  "field_147067_x"), // GuiContainerCreative/currentScroll
         serverEntityTracker("theEntityTracker",          "K",  "field_73062_L"),  // WorldServer/theEntityTracker
                   itemsList("itemList",                  "a",  "field_148330_a"), // ContainerCreative/itemList
               damagedBlocks("damagedBlocks",             "O",  "field_72738_E"),  // RenderGlobal/damagedBlocks
               currentLocale("i18nLocale",                "a",  "field_135054_a"), // I18n/i18nLocale
              translateTable( null,                       "a",  "field_135032_a"), // Locale/field_135032_a
             downloadedImage("bufferedImage",             "g",  "field_110560_d"), // ThreadDownloadImageData/bufferedImage
                  renderZoom("cameraZoom",                "af", "field_78503_V"),  // EntityRenderer/cameraZoom
                renderOfsetX("cameraYaw",                 "ag", "field_78502_W"),  // EntityRenderer/cameraYaw
                renderOfsetY("cameraPitch",               "ah", "field_78509_X"),  // EntityRenderer/cameraPitch
              
           getSlotAtPosition("getSlotAtPosition",         "c",  "func_146975_c"),  // GuiContainer/getSlotAtPosition (
            handleMouseClick("handleMouseClick",          "a",  "func_146984_a"),  // GuiContainer/handleMouseClick (
                   selectTab("setCurrentCreativeTab",     "b",  "func_147050_b"),  // GuiContainerCreative/setCurrentCreativeTab (
                    scrollTo("scrollTo",                  "a",  "func_148329_a"),  // ContainerCreative/scrollTo (
                renderSkyBox("renderSkybox",              "c",  "func_73971_c"),   // GuiMainMenu/renderSkybox
                	  resize("resize",                    "a",  "func_71370_a"),   // Minecraft/resize
        addTileEntityMapping("addMapping",                "a",  "func_145826_a"),  // TileEntity/addMapping
       guiScreenMouseClicked("mouseClicked",              "a",  "func_73864_a"),   // GuiScreen/mouseClicked
     guiScreenMouseMovedOrUp("mouseMovedOrUp",            "b",  "func_146286_b"),  // GuiScreen/mouseMovedOrUp
           guiScreenKeyTyped("keyTyped",                  "a",  "func_73869_a"),   // GuiScreen/keyTyped
              
         theIntegratedServer("theIntegratedServer",       "X",  "field_71437_Z"),  // Minecraft/theIntegratedServer
   integratedServerIsRunning("integratedServerIsRunning", "al", "field_71455_al"), // Minecraft/integratedServerIsRunning
            myNetworkManager("myNetworkManager",          "ak", "field_71453_ak"), // Minecraft/myNetworkManager
     guiScreenSelectedButton("selectedButton",            "a",  "field_146290_a"), // GuiScreen/selectedButton
              worldRenderers("worldRenderers",            "v",  "field_72765_l"),  // RenderGlobal/worldRenderers
                   worldType("terrainType",               "b",  "field_76098_b"),  // WorldInfo/terrainType
            fontRendererPosY("posY",                      "j",  "field_78296_k"),  // FontRenderer/posY
               worldSelected( null,                       "i",  "field_146634_i"), // GuiSelectWorld/selected
        guiSelectWorldParent("parentScreen",              "a",  "field_146632_a"), // GuiSelectWorld/parentScreen
              modelBipedMain("modelBipedMain",            "f",  "field_77109_a"),  // RenderPlayer/modelBipedMain
        modelArmorChestplate("modelArmorChestplate",      "g",  "field_77108_b"),  // RenderPlayer/modelArmorChestplate
                  modelArmor("modelArmor",                "h",  "field_77111_i"),  // RenderPlayer/modelArmor
                spawnerLogic( null,                       "a",  "field_145882_a"), // TileEntityMobSpawner/field_145882_a
          specialRendererMap("mapSpecialRenderers",       "m",  "field_147559_m"), // TileEntityRendererDispatcher/mapSpecialRenderers
           soundSystemThread("sndSystem",                 "e",  "field_148620_e"), // SoundManager/sndSystem
                 lastClicked("lastClicked",               "q",  "field_148167_s"), // GuiSlot/lastClicked

                 skinTexture("downloadImageSkin",         "a",  "field_110316_a"), // AbstractClientPlayer/downloadImageSkin
                cloakTexture("downloadImageCape",         "c",  "field_110315_c"), // AbstractClientPlayer/downloadImageCape
                skinResource("locationSkin",              "d",  "field_110312_d"), // AbstractClientPlayer/locationSkin
               cloakResource("locationCape",              "e",  "field_110313_e"), // AbstractClientPlayer/locationCape
  
        resourceToTextureMap("mapTextureObjects",         "b",  "field_110585_a"), // TextureManager/mapTextureObjects
        
    tileEntityNameToClassMap("nameToClassMap",            "i",  "field_145855_i"), // TileEntity/nameToClassMap
                realmsButton("field_96139_s",             "x",  "field_96139_s"),  // GuiMainMenu/field_96139_s
           optionsBackground("optionsBackground",         "b",  "field_110325_k"), // Gui/optionsBackground
             panoramaTexture( null,                       "L",  "field_110351_G"), // GuiMainMenu/field_110351_G
             
                    lastPosZ("lastPosZ",                  "o",  "field_147373_o"), // NetServerHandler/lastPosX
                    lastPosX("lastPosX",                  "p",  "field_147382_p"), // NetServerHandler/lastPosY
                    lastPosY("lastPosY",                  "q",  "field_147381_q"), // NetServerHandler/lastPosZ
                    hasMoved("hasMoved",                  "r",  "field_147380_r"), // NetServerHandler/hasMoved

             rainingStrength("rainingStrength",           "n",  "field_73004_o"),   // World/rainingStrength
          thunderingStrength("thunderingStrength",        "p",  "field_73017_q"),   // World/thunderingStrength
          
               standing_sign("standing_sign",             "an", "field_150472_an"), // Blocks/standing_sign
                   wall_sign("wall_sign",                 "as", "field_150444_as"), // Blocks/wall_sign
                 mob_spawner("mob_spawner",               "ac", "field_150474_ac"), // Blocks/mob_spawner

          internetServerList( null,                       "i",  "field_146804_i"),  // GuiMultiplayer/internetServerList
         serverSelectionList( null,                       "h",  "field_146803_h"),  // GuiMultiplayer/selectedServer
guiResourcePacksParentScreen( null,                       "f",  "field_146965_f"),  // GuiScreenResourcePacks/parentScreen
    abstractResourcePackFile("resourcePackFile",          "a",  "field_110597_b"),  // AbstractResourcePack/resourcePackFile
               mcFramebuffer("framebufferMc",             "at", "field_147124_at"), // Minecraft/mcFramebuffer
                 eventSounds("soundPool",                 "a",  "field_148736_a"),  // SoundEventAccessorComposite/field_148736_a
                   worldInfo("worldInfo",                 "x",  "field_72986_A"),   // World/worldInfo
                   
       SlotCreativeInventory( null,                     "bdt", "net.minecraft.client.gui.inventory.GuiContainerCreative$CreativeSlot"),
           ContainerCreative( null,                     "bds", "net.minecraft.client.gui.inventory.GuiContainerCreative$ContainerCreative"),
    CallableMinecraftVersion( null,                       "e", "net.minecraft.crash.CrashReport$1");

	public final String mcpName;
	
	public final String obfuscatedName;
	
	public final String seargeName;
	
	private ObfuscationMapping(String mcpName, String obfuscatedName, String seargeName)
	{
		this.mcpName        = mcpName != null ? mcpName : seargeName;
		this.obfuscatedName = obfuscatedName;
		this.seargeName     = seargeName;
	}

	public String getName()
	{
		return ModUtilities.getObfuscatedFieldName(this.mcpName, this.obfuscatedName, this.seargeName);
	}
}
