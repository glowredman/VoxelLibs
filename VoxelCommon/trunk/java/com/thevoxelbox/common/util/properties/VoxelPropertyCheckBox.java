package com.thevoxelbox.common.util.properties;

import com.thevoxelbox.common.LiteModVoxelCommon;
import com.thevoxelbox.common.interfaces.IVoxelPropertyProvider;
import com.thevoxelbox.common.util.gui.IAdvancedDrawGui;

/**
 * Adapted from xTiming's checkbox code
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPropertyCheckBox extends VoxelPropertyToggleButton
{
	private int width = 11;
	
	public VoxelPropertyCheckBox(IVoxelPropertyProvider propertyProvider, String binding, String text, int xPos, int yPos)
	{
		super(propertyProvider, binding, text, xPos, yPos);
		
		this.width = this.fontRenderer.getStringWidth(this.displayText) + 20;
	}
	
	@Override
	public void draw(IAdvancedDrawGui host, int mouseX, int mouseY)
	{
		this.drawString(this.fontRenderer, this.displayText, this.xPosition + 20, this.yPosition + 2, 0xFFFFFF);
		
		boolean overButton = this.mouseOver(mouseX, mouseY);
		boolean checked = this.propertyProvider.getBoolProperty(this.propertyBinding);
		
		host.drawTessellatedModalBorderRect(LiteModVoxelCommon.GUIPARTS, 256, this.xPosition, this.yPosition, this.xPosition + 11, this.yPosition + 11, 0, overButton ? 16 : 0, 16, overButton ? 32 : 16, 4);
		host.drawTexturedModalRect(LiteModVoxelCommon.GUIPARTS, this.xPosition, this.yPosition, this.xPosition + 10, this.yPosition + 10, checked ? 12 : 0, 52, checked ? 23 : 11, 63);
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY)
	{
		return mouseX > this.xPosition && mouseX < this.xPosition + this.width && mouseY > this.yPosition && mouseY < this.yPosition + 11;
	}
}
