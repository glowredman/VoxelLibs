package com.thevoxelbox.common.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Utility Class for building a chat message based on StringBuilder
 * @author thatapplefreak
 * 
 * TODO Flesh out this class to actually do something useful
 */
public class ChatMessageBuilder
{
	/**
	 * The message being built
	 */
	IChatComponent queuedMessage;
	
	public ChatMessageBuilder()
	{
		this.queuedMessage = new ChatComponentText("");
	}
	
	/**
	 * Just add some plain text into the message
	 * @param text Text to add
	 */
	public ChatMessageBuilder append(String text)
	{
		this.queuedMessage.appendSibling(new ChatComponentText(text));
		return this;
	}
	
	/**
	 * Add some formatted text into the message
	 * 
	 * @param text The text in the message
	 * @param color The color in the message
	 * @param underline Underline the text?
	 */
	public ChatMessageBuilder append(String text, EnumChatFormatting color, boolean underline)
	{
		IChatComponent addmsg = new ChatComponentText(text);
		addmsg.getChatStyle().setColor(color);
		addmsg.getChatStyle().setUnderlined(underline);
		this.queuedMessage.appendSibling(addmsg);
		return this;
	}
	
	/**
	 * @param comp
	 * @return
	 */
	public ChatMessageBuilder append(IChatComponent comp)
	{
		this.queuedMessage.appendSibling(this.queuedMessage);
		return this;
	}
	
	/**
	 * Add a URL link into the message
	 * @param text The Link Text
	 * @param path The URL
	 */
	public ChatMessageBuilder append(String text, String path, boolean onAWebsite)
	{
		this.append(text, EnumChatFormatting.WHITE, path, onAWebsite);
		return this;
	}
	
	/**
	 * Add a URL link into the message with color
	 * @param text The Link Text
	 * @param color Color of the link
	 * @param path The URL
	 */
	public ChatMessageBuilder append(String text, EnumChatFormatting color, String path, boolean onAWebsite)
	{
		IChatComponent addmsg = new ChatComponentText(text);
		addmsg.getChatStyle().setColor(color);
		addmsg.getChatStyle().setUnderlined(true);
		addmsg.getChatStyle().setChatClickEvent(new ClickEvent(onAWebsite ? Action.OPEN_URL : Action.OPEN_FILE, path));
		this.queuedMessage.appendSibling(addmsg);
		return this;
	}
	
	/**
	 * Put the message into chat
	 */
	public void showChatMessageIngame()
	{
		AbstractionLayer.getIngameGui().getChatGUI().printChatMessage(this.queuedMessage);
	}
}