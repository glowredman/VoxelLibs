package com.thevoxelbox.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.thevoxelbox.common.interfaces.IListObject;

/**
 * Information about an enumerated item, for display in the item list
 * 
 * @author Adam Mummery-Smith
 */
public class ItemStackInfo implements IListObject
{
	protected IIcon icon;
	
	/**
	 * Item name
	 */
	protected String name, nameWithID;
	
	protected int damageValue;
	
	protected ItemStack stack;
	
	/**
	 * Create a new item info
	 * 
	 * @param id Item ID
	 * @param texture Item texture
	 * @param icon Item's icon index
	 */
	public ItemStackInfo(IIcon icon, int damage, ItemStack stack)
	{
		this.name = stack.getDisplayName();
		this.icon = icon;
		this.damageValue = damage;
		this.stack = stack;
	}
	
	/**
	 * 
	 */
	public void updateName()
	{
		this.name = this.stack.getDisplayName();
		this.nameWithID = this.name + " " + Item.getIdFromItem(this.stack.getItem());
		
		if (this.damageValue > 0)
			this.nameWithID += ":" + this.damageValue;
	}

	/**
	 * True if the item has an icon that can be displayed in list boxes
	 */
	@Override
	public boolean hasIcon()
	{
		return true;
	}

	@Override
	public IIcon getIcon()
	{
		return this.icon;
	}
	
	public String getName(boolean withID)
	{
		return withID ? this.nameWithID : this.name;
	}
	
	public int getDamage()
	{
		return this.damageValue;
	}

	public ItemStack getItemStack()
	{
		return this.stack;
	}
	
	public boolean compareTo(ItemStackInfo other)
	{
		return (other.name.equalsIgnoreCase(this.name) && other.icon == this.icon);
	}

	/**
	 * Get the display text to show in listboxes
	 * 
	 * @return display text
	 */
	@Override
	public String getText()
	{
		return this.name;
	}

	/**
	 * Get the display text to show in listboxes
	 * 
	 * @return display text
	 */
	public String getSortText()
	{
		String sortText = this.name;
		
		if (this.stack.getItem() instanceof ItemFood) sortText += " food";
		if (this.name.toLowerCase().contains("wood")) sortText += " wood";
		
		return sortText;
	}
	
	/**
	 * Get the item's ID
	 */
	@Override
	public int getID()
	{
		return Item.getIdFromItem(this.stack.getItem());
	}

	/**
	 * Get the data associated with the item
	 */
	@Override
	public Object getData()
	{
		return null;
	}

	/**
	 * Return the custom draw behaviour for the item
	 */
	@Override
	public CustomDrawBehaviour getCustomDrawBehaviour()
	{
		return CustomDrawBehaviour.NoCustomDraw;
	}

	/**
	 * Handles custom drawing, this object does not support custom draw behaviour
	 */
	@Override
	public void drawCustom(boolean iconEnabled, Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width, int height, int updateCounter)
	{
	}

	/**
	 * Handle mouse pressed, this object does not support this event
	 */
	@Override
	public boolean mousePressed(boolean iconEnabled, Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width,int height)
	{
		return false;
	}

	/**
	 * Handle mouse released.
	 */
	@Override
	public void mouseReleased(int mouseX, int mouseY)
	{
	}

	/**
	 * Get custom action. Not supported by this object, always returns an empty string.
	 */
	@Override
	public String getCustomAction(boolean bClear)
	{
		return "";
	}

	/**
	 * Get whether this object is draggable or not
	 */
	@Override
	public boolean getDraggable()
	{
		return false;
	}

	@Override
	public void setIconTexture(String newTexture)
	{
	}

	@Override
	public void setIconID(int newIconId)
	{
	}

	@Override
	public void setText(String newText)
	{
		this.name = newText;
		
	}

	@Override
	public void setID(int newId)
	{
	}

	@Override
	public void setData(Object newData)
	{
	}

	@Override
	public boolean getCanEditInPlace()
	{
		return false;
	}

	@Override
	public boolean getEditingInPlace()
	{
		return false;
	}

	@Override
	public void beginEditInPlace()
	{
	}

	@Override
	public void endEditInPlace()
	{
	}

	@Override
	public boolean editInPlaceKeyTyped(char keyChar, int keyCode)
	{
		return false;
	}

	@Override
	public void editInPlaceDraw(boolean iconEnabled, Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width, int height, int updateCounter)
	{
	}

	@Override
	public boolean editInPlaceMousePressed(boolean iconEnabled, Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width, int height)
	{
		return false;
	}

	@Override
	public String serialise()
	{
		return null;
	}

	@Override
	public void bindIconTexture()
	{
	}

	@Override
	public int getIconSize()
	{
		return 16;
	}

	@Override
	public int getIconTexmapSize()
	{
		return 256;
	}

	@Override
	public String getDisplayName()
	{
		return this.name.toLowerCase();
	}

	@Override
	public void setDisplayName(String newDisplayName)
	{
	}
}
