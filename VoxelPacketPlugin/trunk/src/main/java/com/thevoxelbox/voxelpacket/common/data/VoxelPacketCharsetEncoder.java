package com.thevoxelbox.voxelpacket.common.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * Raw byte encoder for marshalling chars directly to bytes, could probably use the unicode encoder but I don't trust it
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPacketCharsetEncoder extends CharsetEncoder
{
	/**
	 * Create a new encoder 
	 * 
	 * @param cs
	 */
	public VoxelPacketCharsetEncoder(Charset cs)
	{
		super(cs, 2, 2);
	}

	@Override
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out)
	{
		while (in.remaining() > 0)
		{
			out.put(Marshal.charToByte(in.get()));
		}
		
		return CoderResult.UNDERFLOW;
	}
}
