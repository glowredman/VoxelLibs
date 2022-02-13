package com.thevoxelbox.common.util.properties;

import com.thevoxelbox.common.LiteModVoxelCommon;
import com.thevoxelbox.common.interfaces.IVoxelPropertyProvider;
import com.thevoxelbox.common.interfaces.IVoxelPropertyProviderBoolean;
import com.thevoxelbox.common.util.gui.IAdvancedDrawGui;

/**
 * Adapted from xTiming's button code
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPropertyToggleButton extends VoxelProperty<IVoxelPropertyProviderBoolean>
{
	private int buttonOffset = 60; 
	private int buttonWidth = 70;
	private int buttonHeight = 16;

	private String status;
	
	/**
	 * @param propertyProvider
	 * @param binding
	 * @param displayText
	 * @param xPos
	 * @param yPos
	 */
	public VoxelPropertyToggleButton(IVoxelPropertyProvider propertyProvider, String binding, String displayText, int xPos, int yPos)
	{
		this(propertyProvider, binding, displayText, xPos, yPos, 60, 70, 16);
	}
	
	/**
	 * @param propertyProvider
	 * @param binding
	 * @param displayText
	 * @param xPos
	 * @param yPos
	 * @param buttonOffset
	 * @param buttonWidth
	 * @param buttonHeight
	 */
	public VoxelPropertyToggleButton(IVoxelPropertyProvider propertyProvider, String binding, String displayText, int xPos, int yPos, int buttonOffset, int buttonWidth, int buttonHeight)
	{
		super(propertyProvider, binding, displayText, xPos, yPos);
		this.buttonOffset = buttonOffset;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.status = this.propertyProvider.getOptionDisplayString(this.propertyBinding);
	}

	/* (non-Javadoc)
	 * @see com.thevoxelbox.common.util.properties.VoxelProperty#draw(com.thevoxelbox.common.util.gui.IAdvancedDrawGui, int, int)
	 */
	@Override
	public void draw(IAdvancedDrawGui host, int mouseX, int mouseY)
	{
		boolean overButton = this.mouseOver(mouseX, mouseY);
		int outset = overButton ? 1 : 0;
		int v = overButton ? 16 : 0;
		int colour = overButton ? 0xFFFFFF : 0x999999;
		
		this.drawString(this.fontRenderer, this.displayText, this.xPosition, this.yPosition + 4, 0xFFFFFF);
		drawRect(this.xPosition + this.buttonOffset + this.buttonWidth - 1, this.yPosition + this.buttonHeight, this.xPosition + this.buttonOffset + 1, this.yPosition, 0xFF000000);
		host.drawTessellatedModalBorderRect(LiteModVoxelCommon.GUIPARTS, 256, this.xPosition + this.buttonOffset - outset, this.yPosition - outset, this.xPosition + this.buttonOffset + this.buttonWidth + outset, this.yPosition + this.buttonHeight + outset, 0, v, 16, v + 16, 4);
		this.drawString(this.fontRenderer, this.status, (this.xPosition + this.buttonOffset + (this.buttonWidth / 2)) - (this.fontRenderer.getStringWidth(this.status) / 2), this.yPosition + 4, colour);
	}

	/* (non-Javadoc)
	 * @see com.thevoxelbox.common.util.properties.VoxelProperty#mouseClicked(int, int)
	 */
	@Override
	public void mouseClicked(int mouseX, int mouseY)
	{
		if (this.mouseOver(mouseX, mouseY))
		{
			this.propertyProvider.toggleOption(this.propertyBinding);
			this.status = this.propertyProvider.getOptionDisplayString(this.propertyBinding);
		}
	}

	/**
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean mouseOver(int mouseX, int mouseY)
	{
		return mouseX > this.xPosition + this.buttonOffset && mouseX < this.xPosition + this.buttonOffset + this.buttonWidth && mouseY >= this.yPosition && mouseY <= this.yPosition + this.buttonHeight;
	}
	
	/* (non-Javadoc)
	 * @see com.thevoxelbox.common.util.properties.VoxelProperty#keyTyped(char, int)
	 */
	@Override
	public void keyTyped(char keyChar, int keyCode)
	{
	}
}
