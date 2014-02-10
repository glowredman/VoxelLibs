package com.thevoxelbox.common.util.gui;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import com.thevoxelbox.common.LiteModVoxelCommon;
import com.thevoxelbox.common.util.AbstractionLayer;

/**
 * Simple checkbox-like control
 * 
 * @author Adam Mummery-Smith
 */
public class GuiCheckBox extends GuiControl
{
	public enum DisplayStyle
	{
		Button,
		CheckBox,
		KeyboardKey
	}
	
	public DisplayStyle Style = DisplayStyle.CheckBox;
	
	/**
	 * True if the checkbox is checked
	 */
	public boolean checked;
	
	/**
	 * Make a new check box control, automatically size the control to fit the specified text
	 * 
	 * @param id Control ID
	 * @param xPosition X location for the control
	 * @param yPosition Y location for the control
	 * @param displayText Text to display
	 * @param checked Initially checked value
	 */
	public GuiCheckBox(int id, int xPosition, int yPosition, String displayText, boolean checked)
	{
		super(id, xPosition, yPosition, displayText);
		
		this.setWidth(Minecraft.getMinecraft().fontRenderer.getStringWidth(displayText) + 20);
		this.checked = checked;
	}
	
	/**
	 * Make a new check box control, automatically size the control to fit the specified text
	 * 
	 * @param id Control ID
	 * @param xPosition X location for the control
	 * @param yPosition Y location for the control
	 * @param displayText Text to display
	 * @param checked Initially checked value
	 */
	public GuiCheckBox(int id, int xPosition, int yPosition, String displayText, boolean checked, DisplayStyle style)
	{
		super(id, xPosition, yPosition, displayText);
		
		this.setWidth(Minecraft.getMinecraft().fontRenderer.getStringWidth(displayText) + 20);
		this.checked = checked;
		this.Style = style;
	}
	
	/**
	 * Make a new check box control of the specified size
	 * 
	 * @param id Control ID
	 * @param xPosition X location for the control
	 * @param yPosition Y location for the control
	 * @param width Width for the control (text may overflow or underflow)
	 * @param height Height for the control (check box and text will be centered vertically within this height)
	 * @param displayText Text to display
	 * @param checked Initially checked value
	 */
	public GuiCheckBox(int id, int xPosition, int yPosition, int width, int height, String displayText, boolean checked)
	{
		super(id, xPosition, yPosition, width, height, displayText);
		
		this.checked = checked;
	}

	/**
	 * Make a new check box control of the specified size
	 * 
	 * @param id Control ID
	 * @param xPosition X location for the control
	 * @param yPosition Y location for the control
	 * @param width Width for the control (text may overflow or underflow)
	 * @param height Height for the control (check box and text will be centered vertically within this height)
	 * @param displayText Text to display
	 * @param checked Initially checked value
	 */
	public GuiCheckBox(int id, int xPosition, int yPosition, int width, int height, String displayText, boolean checked, DisplayStyle style)
	{
		super(id, xPosition, yPosition, width, height, displayText);
		
		this.checked = checked;
		this.Style = style;
	}

	public void drawCheckboxAt(Minecraft minecraft, int mouseX, int mouseY, int yPos)
	{
		this.setYPosition(yPos);
		this.drawButton(minecraft, mouseX, mouseY);
	}

	/**
	 * Draw the control
	 * 
	 * @param minecraft Minecraft instance
	 * @param mouseX Mouse x coordinate
	 * @param mouseY Mouse y coordinate
	 */
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
	{
		// Control not visible
		if(!this.isVisible()) return;
		
		boolean mouseOver = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
		
		if (this.Style == DisplayStyle.Button)
		{
			super.drawButton(minecraft, mouseX, mouseY);
		}
		else if (this.Style == DisplayStyle.CheckBox)
		{
			AbstractionLayer.bindTexture(LiteModVoxelCommon.GUIPARTS);
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			int u = this.checked ? 12 : 0;
			int y = this.field_146129_i + (this.field_146121_g - 12) / 2;
			
			this.drawTexturedModalRect(this.field_146128_h, y, this.field_146128_h + 12, y + 12, u, 52, u + 12, 64);
			this.mouseDragged(minecraft, mouseX, mouseY);
			this.drawString(minecraft.fontRenderer, this.displayString, this.field_146128_h + 16, this.field_146129_i + (this.field_146121_g - 8) / 2, this.isEnabled() ? (mouseOver ? 0xa0ffff : 0xe0e0e0) : 0xffa0a0a0);
		}
		else
		{
			Gui.drawRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, this.checked ? 0xFFFFFF00 : 0xFF808080);
			Gui.drawRect(this.field_146128_h + 1, this.field_146129_i + 1, this.field_146128_h + this.field_146120_f - 1, this.field_146129_i + this.field_146121_g - 1, mouseOver ? 0xFF333333 : 0xFF000000);
			this.drawCenteredString(minecraft.fontRenderer, this.displayString, this.field_146128_h + (this.field_146120_f / 2), this.field_146129_i + (this.field_146121_g - 8) / 2, this.isEnabled() ? (mouseOver ? 0xa0ffff : (this.checked ? 0xFFFFFF00 : 0xe0e0e0)) : 0xffa0a0a0);
		}
	}

	/**
	 * @param minecraft
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY)
	{
		if (super.mousePressed(minecraft, mouseX, mouseY))
		{
			this.checked = !this.checked;
			return true;
		}
		
		return false;
	}
}
