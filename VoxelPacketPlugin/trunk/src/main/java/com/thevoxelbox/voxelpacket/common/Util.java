package com.thevoxelbox.voxelpacket.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public abstract class Util
{
	public static Server getServer()
	{
		return Bukkit.getServer();
	}
	
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
	 * Uses reflection to access a private value inside a foreign class
	 * 
	 * @param instanceClass Class which defines the required field
	 * @param instance Instance object to get field
	 * @param fieldName Name of the field to access (inside debugger)
	 * 
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName)
	{
    	return (T)getPrivateValue(instanceClass, instance, getObfuscatedFieldName(fieldName, obfuscatedFieldName));
	}
    
	/**
	 * Uses reflection to access a private value inside a foreign class
	 * 
	 * @param instanceClass Class which defines the required field
	 * @param instance Instance object to get field
	 * @param obfuscatedFieldName Name of the field to access (inside debugger)
	 * 
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String obfuscatedFieldName)
	{
	    try
	    {
	        Field field = instanceClass.getDeclaredField(obfuscatedFieldName);
	        field.setAccessible(true);
	        return (T)field.get(instance);
	    }
	    catch (Exception ex)
	    {
	        return null;
	    }
	}
    
    /**
     * Switch between obfuscated field names and friendly field names at run-time, determines which to return
     * by using reflection to check whether the name for Entity is Entity.
     * 
     * @param fieldName Name of field to get, returned unmodified if in debug mode
     * @return Obfuscated field name if present
     */
    public static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName)
    {
    	return (!Entity.class.getSimpleName().equals("Entity")) ? obfuscatedFieldName : fieldName;
    }
    
    /**
     * Get an entity from an entity ID
     * 
     * @param id
     * @return
     */
    public static Entity getEntityWithID(int id)
    {
    	if (id >= 0 && getServer() != null)
    	{
    		for (World world : getServer().getWorlds())
    		{
    			for (Entity entity : world.getEntities())
    			{
    				if (entity.getEntityId() == id)
    					return entity;
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
    	return getDisplayBytes(subject, "US-ASCII");
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
    @SuppressWarnings("cast")
	public static String getDisplayBytes(byte[] subject)
    {
    	String result = "";
    	
		for (int b = 0; b < subject.length; b++)
		{
			String byteString = Integer.toHexString((int)subject[b] & 0xFF);
			if (byteString.length() == 1)
				byteString = "0" + byteString;
			result += byteString + " ";
		}
		
		return result;
    }
}
