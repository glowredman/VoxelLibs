package com.thevoxelbox.common.util;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

/**
 * Wrapper for obf/mcp reflection-accessed private methods, added to centralise the locations I have to update the obfuscated method names
 * 
 * @author Adam Mummery-Smith
 *
 * @param <P> Parent class type
 * @param <R> Method return type
 */
public class PrivateMethods<P, R>
{
	public static class Void {}
	
	/**
	 * Class to which this field belongs
	 */
	public final Class<?> parentClass;

	/**
	 * Name used to access the field, determined at init
	 */
	private final String methodName;
	
	/**
	 * Method 
	 */
	private final Method method;
	
	/**
	 * Creates a new private field entry
	 * 
	 * @param owner
	 * @param mcpName
	 * @param name
	 */
	private PrivateMethods(Class<?> owner, ObfuscationMapping mapping, Class<?>... parameterTypes)
	{
		this.parentClass = owner;
		this.methodName = mapping.getName();
		
		Method method = null;

		try
		{
			method = this.parentClass.getDeclaredMethod(this.methodName, parameterTypes);
			method.setAccessible(true);
		}
		catch (SecurityException ex)
		{
			ex.printStackTrace();
		}
		catch (NoSuchMethodException ex)
		{
			ex.printStackTrace();
		}
		
		this.method = method;
	}
	
	/**
	 * Invoke the method and return a value
	 * 
	 * @param instance
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public R invoke(P instance, Object... args)
	{
		try
		{
			return (R)this.method.invoke(instance, args);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Invoke a static method that returns a value
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public R invokeStatic(Object... args)
	{
		try
		{
			return (R)this.method.invoke(null, args);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Invoke a method that returns void
	 * 
	 * @param instance
	 * @param args
	 */
	public void invokeVoid(P instance, Object... args)
	{
		try
		{
			this.method.invoke(instance, args);
		}
		catch (Exception ex) {}
	}

	/**
	 * Invoke a static method that returns void
	 * 
	 * @param args
	 */
	public void invokeStaticVoid(Object... args)
	{
		try
		{
			this.method.invoke(null, args);
		}
		catch (Exception ex) {}
	}
	
	// Methods on public classes
	public static final PrivateMethods<GuiContainer, Slot>                   containerGetSlotAtPosition = new PrivateMethods<GuiContainer, Slot>                   (GuiContainer.class,         ObfuscationMapping.getSlotAtPosition,       int.class, int.class                       );
	public static final PrivateMethods<GuiContainer, Void>                    containerHandleMouseClick = new PrivateMethods<GuiContainer, Void>                   (GuiContainer.class,         ObfuscationMapping.handleMouseClick,        Slot.class, int.class, int.class, int.class);
	public static final PrivateMethods<GuiContainerCreative, Void>                            selectTab = new PrivateMethods<GuiContainerCreative, Void>           (GuiContainerCreative.class, ObfuscationMapping.selectTab,               CreativeTabs.class                         );
	public static final PrivateMethods<GuiMainMenu, Void>                          mainMenuRenderSkyBox = new PrivateMethods<GuiMainMenu, PrivateMethods.Void>     (GuiMainMenu.class,          ObfuscationMapping.renderSkyBox,            int.class, int.class, float.class          );
	public static final PrivateMethods<Minecraft, Void>                                 resizeMinecraft = new PrivateMethods<Minecraft, Void>                      (Minecraft.class,            ObfuscationMapping.resize,                  int.class, int.class                       );
	public static final PrivateMethods<TileEntity, Void>                           addTileEntityMapping = new PrivateMethods<TileEntity, Void>                     (TileEntity.class,           ObfuscationMapping.addTileEntityMapping,    Class.class, String.class                  );

	public static final PrivateMethods<GuiScreen, Void>                           guiScreenMouseClicked = new PrivateMethods<GuiScreen, Void>                      (GuiScreen.class,            ObfuscationMapping.guiScreenMouseClicked,   int.class, int.class, int.class            );
	public static final PrivateMethods<GuiScreen, Void>                         guiScreenMouseMovedOrUp = new PrivateMethods<GuiScreen, Void>                      (GuiScreen.class,            ObfuscationMapping.guiScreenMouseMovedOrUp, int.class, int.class, int.class            );
	public static final PrivateMethods<GuiScreen, Void>                               guiScreenKeyTyped = new PrivateMethods<GuiScreen, Void>                      (GuiScreen.class,            ObfuscationMapping.guiScreenKeyTyped,       char.class, int.class                      );
	
	// Methods on dynamic classes below here
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final PrivateMethods<Container, Void> scrollTo = new PrivateMethods(PrivateClasses.ContainerCreative.Class, ObfuscationMapping.scrollTo, float.class);
}