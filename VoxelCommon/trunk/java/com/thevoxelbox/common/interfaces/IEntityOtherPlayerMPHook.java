package com.thevoxelbox.common.interfaces;

import com.thevoxelbox.common.entity.EntityOtherPlayerMPHookable;

/**
 *
 * @author Adam Mummery-Smith
 */
public interface IEntityOtherPlayerMPHook
{
	public abstract boolean setupSkin(String username, EntityOtherPlayerMPHookable other);
}
