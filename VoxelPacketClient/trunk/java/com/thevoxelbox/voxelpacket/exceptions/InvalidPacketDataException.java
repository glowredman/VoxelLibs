package com.thevoxelbox.voxelpacket.exceptions;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class InvalidPacketDataException extends VoxelPacketException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2130182488180470657L;

	public InvalidPacketDataException()
	{
	}
	
	public InvalidPacketDataException(String arg0)
	{
		super(arg0);
	}
	
	public InvalidPacketDataException(Throwable arg0)
	{
		super(arg0);
	}
	
	public InvalidPacketDataException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
