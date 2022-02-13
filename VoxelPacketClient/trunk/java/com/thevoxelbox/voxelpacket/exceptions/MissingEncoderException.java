package com.thevoxelbox.voxelpacket.exceptions;

/**
 * 
 * @author Adam Mummery-Smith
 *
 */
public class MissingEncoderException extends VoxelPacketException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8234715830013200494L;

	public MissingEncoderException()
	{
		super();
	}

	public MissingEncoderException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	public MissingEncoderException(String arg0)
	{
		super(arg0);
	}

	public MissingEncoderException(Throwable arg0)
	{
		super(arg0);
	}
	
}
