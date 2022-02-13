package com.thevoxelbox.voxelpacket.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class Util
{
	/**
	 * Strip colour codes from inbound messages
	 * 
	 * @param text
	 * @return
	 */
	public static String stripColourCodes(String text)
	{
		return text != null ? text.replaceAll("\\xa7[0-9a-fklmnor]", "") : null;
	}

    /**
     * Get an entity from an entity ID
     * 
     * @param id
     * @return
     */
    public static Entity getEntityWithID(int id)
    {
    	if (id >= 0)
    	{
    		World theWorld = Minecraft.getMinecraft().theWorld;
    		
    		if (theWorld != null)
    		{
    			for (Object entity : theWorld.getLoadedEntityList())
    			{
    				if (((Entity)entity).hashCode() == id)
    					return (Entity)entity;
    			}
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Debug function for displaying bytes
     * 
     * @param subject
     * @return
     */
    public static String getDisplayBytes(String subject)
    {
    	return getDisplayBytes(subject, VoxelPacket.CHARSET);
    }

    /**
     * Debug function for displaying bytes
     * 
     * @param subject
     * @param encoding
     * @return
     */
    public static String getDisplayBytes(String subject, String encoding)
    {
    	String result = "";
    	
    	try
    	{
    		result = getDisplayBytes(subject.getBytes(encoding));
    	}
    	catch (UnsupportedEncodingException ex) {}
    	
    	return result;
    }

    /**
     * Debug function for displaying bytes
     * 
     * @param subject
     * @param encoding
     * @return
     */
    public static String getDisplayBytes(String subject, Charset encoding)
    {
		return getDisplayBytes(subject.getBytes(encoding));
    }

    /**
     * Debug function for displaying bytes
     * 
     * @param subject
     * @return
     */
    public static String getDisplayBytes(byte[] subject)
    {
    	String result = "";
    	
		for (int b = 0; b < subject.length; b++)
		{
			@SuppressWarnings("cast")
			String byteString = Integer.toHexString((int)subject[b] & 0xFF);
			if (byteString.length() == 1)
				byteString = "0" + byteString;
			result += byteString + " ";
		}
		
		return result;
    }
}
