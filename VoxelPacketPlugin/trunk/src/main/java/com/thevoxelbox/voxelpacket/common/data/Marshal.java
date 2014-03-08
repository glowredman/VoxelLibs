package com.thevoxelbox.voxelpacket.common.data;


/**
 * Bit-bashing functions for packing and unpacking miscellaneous data types
 * 
 * @author Adam Mummery-Smith
 *
 */
public abstract class Marshal
{
	/**
	 * Pack two short values into an integer
	 * 
	 * @param short1
	 * @param short2
	 * 
	 * @return
	 */
	public static int pack(short short1, short short2)
	{
		return (short1 << 16) | short2;
	}
	
	/**
	 * Pack four byte values into an integer
	 * 
	 * @param byte1
	 * @param byte2
	 * @param byte3
	 * @param byte4
	 * 
	 * @return
	 */
	public static int pack(byte byte1, byte byte2, byte byte3, byte byte4)
	{
		return (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4;
	}
	
	/**
	 * Pack two char values into an integer
	 * 
	 * @param char1
	 * @param char2
	 * 
	 * @return
	 */
	public static int pack(char char1, char char2)
	{
		return pack((short)char1, (short)char2);
	}
	
	/**
	 * Pack 32 boolean flags into an integer
	 * 
	 * @param flags
	 * 
	 * @return
	 */
	public static int pack(boolean[] flags)
	{
		if (flags.length > 32)
			throw new ArrayIndexOutOfBoundsException(flags.length);
		
		int packed = 0;
		
		for (int shift = 0; shift < flags.length; shift++)
			if (flags[shift]) packed |= 1 << (31 - shift);
		
		return packed;
	}
	
	/**
	 * Pack 2 integers into a long
	 * 
	 * @param int1
	 * @param int2
	 * @return
	 */
	public static long pack(int int1, int int2)
	{
		return ((long)int1 << 32) | int2;
	}
	
	/**
	 * Unpack an integer into two shorts
	 * 
	 * @param intValue Value to unpack
	 * 
	 * @return
	 */
	public static short[] unpackShort(int intValue)
	{
		return new short[]
		{
			(short)((intValue & 0xFFFF0000) >> 16),
			(short) (intValue & 0x0000FFFF)
		};
	}
	
	/**
	 * Unpack an integer into four bytes
	 * 
	 * @param intValue Value to unpack
	 * 
	 * @return
	 */
	public static byte[] unpackByte(int intValue)
	{
		return new byte[]
		{
			(byte)((intValue & 0xFF000000) >> 24),
			(byte)((intValue & 0xFF0000) >> 16),
			(byte)((intValue & 0xFF00) >> 8),
			(byte) (intValue & 0xFF)
		};
	}
	
	/**
	 * Unpack a long into four bytes
	 * 
	 * @param longValue Value to unpack
	 * 
	 * @return
	 */
	public static byte[] unpackByte(long longValue)
	{
		return new byte[]
		{
			(byte)((longValue & 0xFF00000000000000L) >> 56),
			(byte)((longValue & 0xFF000000000000L) >> 48),
			(byte)((longValue & 0xFF0000000000L) >> 40),
			(byte)((longValue & 0xFF00000000L) >> 32),
			(byte)((longValue & 0xFF000000L) >> 24),
			(byte)((longValue & 0xFF0000L) >> 16),
			(byte)((longValue & 0xFF00L) >> 8),
			(byte) (longValue & 0xFF)
		};
	}
	
	/**
	 * Unpack an integer into two chars
	 * 
	 * @param intValue Value to unpack
	 * 
	 * @return
	 */
	public static char[] unpackChar(int intValue)
	{
		return new char[]
 		{
 			(char)((intValue & 0xFFFF0000) >> 16),
 			(char) (intValue & 0x0000FFFF)
 		};
	}
	
	/**
	 * Unpack an integer into 32 boolean flags
	 *
	 * @param intValue Value to unpack
	 * 
	 * @return unpacked booleans
	 */
	public static boolean[] unpackBoolArray(int intValue)
	{
		boolean[] unpacked = new boolean[32];
		
		for (int shift = 0; shift < 32; shift++)
		{
			int mask = 1 << (31 - shift);
			unpacked[shift] = (intValue & mask) == mask;
		}
		
		return unpacked;
	}
	
	/**
	 * Unpack a long into 2 integers
	 * 
	 * @param longValue
	 * @return
	 */
	public static int[] unpackInt(long longValue)
	{
		return new int[]
		{
			(int)((longValue & 0xFFFFFFFF00000000L) >> 32),
			(int) (longValue & 0x00000000FFFFFFFFL)
		};
	}
	
	public static byte[] charToByte(int inChar)
	{
		return new byte[]
		{
			(byte)((inChar & 0xFF00) >> 8),
			(byte) (inChar & 0xFF)
		};
	}

	public static char byteToChar(byte msb, byte lsb)
	{
		short sMsb = (short)((msb << 8) | (lsb & 0xFF));
		
//		byte[] bt = UnpackByte(Pack((short)0, sMsb));
//		System.out.print("< " + Util.GetDisplayBytes(bt) + ">");
//		
		return (char)(sMsb);
	}
}
