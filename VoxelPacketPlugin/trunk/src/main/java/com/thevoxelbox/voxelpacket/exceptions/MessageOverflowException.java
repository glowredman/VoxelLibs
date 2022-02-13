package com.thevoxelbox.voxelpacket.exceptions;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class MessageOverflowException extends VoxelPacketException
{
	private static final long serialVersionUID = -2964712588443714563L;
	
	public MessageOverflowException()
	{
	}
	
	public MessageOverflowException(String arg0)
	{
		super(arg0);
	}
	
	public MessageOverflowException(Throwable arg0)
	{
		super(arg0);
	}
	
	public MessageOverflowException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
}
