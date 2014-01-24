package com.thevoxelbox.common.status;

import com.thevoxelbox.common.gui.GuiVoxelBoxSettingsPanel;
import com.thevoxelbox.common.util.properties.VoxelPropertyCheckBox;
import com.thevoxelbox.common.util.properties.VoxelPropertyLabel;

/**
 *
 * @author anangrybeaver
 */
public class StatusMessageGUI extends GuiVoxelBoxSettingsPanel
{
	public StatusMessageGUI()
	{
		this.config = StatusMessageManager.getInstance().getConfig();

		this.properties.add(new VoxelPropertyLabel("Status Settings", PANEL_LEFT + 15, PANEL_TOP + 10));
		this.properties.add(new VoxelPropertyCheckBox(this.config, "showStatuses", "Show Statuses",   PANEL_LEFT + 20, this.getRowYPos(0)));
	}
	
	public int getRowYPos(int row)
	{
		return 26 + (row * 20);
	}
	
	@Override
	public String getPanelTitle()
	{
		return "Voxel Status Message";
	}
}