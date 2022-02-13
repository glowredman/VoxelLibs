package com.thevoxelbox.voxelpacket.common.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * Raw charset encoder for stuffing bytes into char arrays. This is to work around the fact that the unicode
 * decoder treats anything that looks like a BOM as a BOM! We also pad the data to an even length if needed
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketCharsetDecoder extends CharsetDecoder
{
	/**
	 * Create a new charset decoder
	 * 
	 * @param cs Parent character set
	 */
	public VoxelPacketCharsetDecoder(Charset cs)
	{
		super(cs, 1, 1);
	}

	@Override
	protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out)
	{
		byte[] bytes = new byte[2];
		
		while (in.remaining() > 1)
		{
			bytes[0] = in.get();
			bytes[1] = in.get();
			
			out.put(Marshal.byteToChar(bytes[0], bytes[1]));
		}
		
		if (in.remaining() == 1)
		{
			bytes[0] = in.get();
			bytes[1] = (byte)0;
			
			out.put(Marshal.byteToChar(bytes[0], bytes[1]));
		}
		
		if (in.remaining() > 0)
		{
			throw new RuntimeException("Error decoding data");
		}
		
		return CoderResult.UNDERFLOW;
	}
}
