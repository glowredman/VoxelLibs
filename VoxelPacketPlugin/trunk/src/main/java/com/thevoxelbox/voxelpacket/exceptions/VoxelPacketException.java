package com.thevoxelbox.voxelpacket.exceptions;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketException extends Exception
{
	private static final long serialVersionUID = -2778867798137967221L;

	public VoxelPacketException()
	{
	}
	
	public VoxelPacketException(String arg0)
	{
		super(arg0);
	}
	
	public VoxelPacketException(Throwable arg0)
	{
		super(arg0);
	}
	
	public VoxelPacketException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
