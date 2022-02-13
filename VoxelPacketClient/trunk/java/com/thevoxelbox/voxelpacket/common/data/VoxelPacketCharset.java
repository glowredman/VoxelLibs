package com.thevoxelbox.voxelpacket.common.data;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * This charset is passed to the string transformation routines to provide the access to the relevant
 * encoder and decoder objects that we want to use
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketCharset extends Charset
{
	public VoxelPacketCharset()
	{
		super("VoxelPacket", new String[0]);
	}
	
	@Override
	public boolean contains(Charset cs)
	{
		return (cs instanceof VoxelPacketCharset);
	}
	
	@Override
	public CharsetDecoder newDecoder()
	{
		// Bytes to chars
		return new VoxelPacketCharsetDecoder(this);
	}
	
	@Override
	public CharsetEncoder newEncoder()
	{
		// Chars to bytes
		return new VoxelPacketCharsetEncoder(this);
	}

	@Override
	public boolean canEncode()
	{
		return true;
	}
}
