package com.thevoxelbox.voxelpacket.exceptions;

public class PartialMessageException extends VoxelPacketException
{
	private static final long serialVersionUID = -290180467394247444L;
	
	public PartialMessageException()
	{
	}
	
	public PartialMessageException(String arg0)
	{
		super(arg0);
	}
	
	public PartialMessageException(Throwable arg0)
	{
		super(arg0);
	}
	
	public PartialMessageException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	
}
