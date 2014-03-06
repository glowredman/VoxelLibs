package com.thevoxelbox.voxelpacket.exceptions;

public class InvalidReplicationException extends VoxelPacketException
{
	private static final long serialVersionUID = 744847463118203723L;

	public InvalidReplicationException()
	{
	}
	
	public InvalidReplicationException(String arg0)
	{
		super(arg0);
	}
	
	public InvalidReplicationException(Throwable arg0)
	{
		super(arg0);
	}
	
	public InvalidReplicationException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
