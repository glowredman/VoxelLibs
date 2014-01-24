package com.thevoxelbox.common.util.properties;

import com.thevoxelbox.common.interfaces.IVoxelPropertyProvider;
import com.thevoxelbox.common.util.gui.IAdvancedDrawGui;

/**
 * Label
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPropertyLabel extends VoxelProperty<IVoxelPropertyProvider>
{
	private int colour = 0x99CCFF;
	
	public VoxelPropertyLabel(String displayText, int xPos, int yPos)
	{
		this(displayText, xPos, yPos, 0x99CCFF);
	}
	
	public VoxelPropertyLabel(String displayText, int xPos, int yPos, int colour)
	{
		super(null, null, displayText, xPos, yPos);
		this.colour = colour;
	}
	
	@Override
	public void draw(IAdvancedDrawGui host, int mouseX, int mouseY)
	{
		this.drawString(this.mc.fontRenderer, this.displayText, this.xPosition, this.yPosition, this.colour);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY)
	{
	}
	
	@Override
	public void keyTyped(char keyChar, int keyCode)
	{
	}
}
