package com.thevoxelbox.common.util;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;


/**
 * Wrapper for obf/mcp reflection-accessed private (or default) classes, added to centralise the locations I have to
 * update the obfuscated names and also make referencing the hidden classes more straightforward
 * 
 * @author Adam Mummery-Smith
 *
 * @param <C> Parent class type
 */
public class PrivateClasses<C>
{
	/**
	 * Class to which this field belongs
	 */
	public final Class<? extends C> Class;
	
	/**
	 * Name used to access the field, determined at init
	 */
	private final String className;
	
	@SuppressWarnings("unchecked")
	private PrivateClasses(ObfuscationMapping mapping)
	{
		this.className = mapping.getName();
		
		Class<? extends C> reflectedClass = null;
		
		try
		{
			reflectedClass = (Class<? extends C>)java.lang.Class.forName(this.className);
		}
		catch (Exception ex) { ex.printStackTrace(); }

		this.Class = reflectedClass;
	}
	
	public static final PrivateClasses<Slot>      SlotCreativeInventory = new PrivateClasses<Slot>      (ObfuscationMapping.SlotCreativeInventory);
	public static final PrivateClasses<Container>     ContainerCreative = new PrivateClasses<Container> (ObfuscationMapping.ContainerCreative);
}
