package com.thevoxelbox.common.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Utility Class for building a chat message based on StringBuilder
 * @author thatapplefreak
 */
public class ChatMessageBuilder {

	/**
	 * The message being built
	 */
	IChatComponent queuedMessage;
	
	public ChatMessageBuilder() {
		queuedMessage = new ChatComponentText("");
	}

	/**
	 * Just add some plain text into the message
	 * @param text Text to add
	 */
	public void appendText(String text) {
		queuedMessage.appendSibling(new ChatComponentText(text));
	}
	
	/**
	 * Add some formatted text into the message
	 * @param text The text in the message
	 * @param color The color in the message
	 * @param underline Underline the text?
	 */
	public void appendText(String text, EnumChatFormatting color, boolean underline) {
		IChatComponent addmsg = new ChatComponentText(text);
		addmsg.getChatStyle().setColor(color);
		addmsg.getChatStyle().setUnderlined(underline);
		queuedMessage.appendSibling(addmsg);
	}
	
	public void appendChatComponent(IChatComponent comp) {
		queuedMessage.appendSibling(queuedMessage);
	}
	
	/**
	 * Add a URL link into the message
	 * @param text The Link Text
	 * @param path The URL
	 */
	public void appendLink(String text, String path) {
		appendLink(text, EnumChatFormatting.WHITE, path);
	}
	
	/**
	 * Add a URL link into the message with color
	 * @param text The Link Text
	 * @param color Color of the link
	 * @param path The URL
	 */
	public void appendLink(String text, EnumChatFormatting color, String path) {
		IChatComponent addmsg = new ChatComponentText(text);
		addmsg.getChatStyle().setColor(color);
		addmsg.getChatStyle().setUnderlined(true);
		addmsg.getChatStyle().setChatClickEvent(new ClickEvent(Action.OPEN_FILE, path));
		queuedMessage.appendSibling(addmsg);
	}
	
	/**
	 * Put the message into chat
	 */
	public void showChatMessageIngame() {
		AbstractionLayer.getIngameGui().getChatGUI().printChatMessage(queuedMessage);
	}
	
}