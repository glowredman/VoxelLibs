package com.thevoxelbox.voxelpacket.exceptions;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class InvalidShortcodeException extends VoxelPacketException
{
	private static final long serialVersionUID = -6767554508014507461L;
	
	public InvalidShortcodeException()
	{
	}
	
	public InvalidShortcodeException(String arg0)
	{
		super(arg0);
	}
	
	public InvalidShortcodeException(Throwable arg0)
	{
		super(arg0);
	}
	
	public InvalidShortcodeException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
