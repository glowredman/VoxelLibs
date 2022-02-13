package com.thevoxelbox.voxelpacket.client;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

import com.thevoxelbox.voxelpacket.common.Util;
import com.thevoxelbox.voxelpacket.common.interfaces.IChatHandler;
import com.thevoxelbox.voxelpacket.common.interfaces.IChatPublisher;
import com.thevoxelbox.voxelpacket.common.interfaces.IChatSubscriber;

/**
 * Broker for incoming chat messages and dispaching them to subscribing objects
 * 
 * @author Adam Mummery-Smith
 * @author MamiyaOtaru
 */
public class ChatManager implements IChatHandler, IChatPublisher
{
	/*
	 * catches:
	 * <name> text
	 * [world <name> text
	 * <[rank] name> text
	 * [world] <[rank] name> text
	 * <name (staff)> text
	 * [world <name (staff)> text
	 * <[rank] name (staff)> text
	 * [world] [subworld] <[rank] name (staff)> text
	 * <rank name> text
	 * <[rank] rank2 name (staff)> text
	 * etc.  basically last (non round bracket surrounded) word in < >, where the < > block may be preceded by any number of square bracket surrounded words
	 * Examples: default, mc.westeroscraft.com
	 */
	private static final Pattern vanillaPlus = Pattern.compile("^(?:\\[[^\\]]*\\]\\s*)*<(?:[^>]*[^>\\w])?(\\w{2,16})(?:\\s*\\([^\\)]*\\))?>+\\s*(.*)"); // name is 1

	/*
	 * catches:
	 * name: text
	 * [rank] name: text
	 * [rank] name (staff): text
	 * *rank* name: text
	 * $*rank*$ name: text
	 * [rank] #$rank2$# name: text
	 * etc. basically last (non round bracket surrounded) word before :, which can be preceded by any number of square bracket or balanced symbol (1 or 2) surrounded words
	 * examples: bukkit default, 
	 */
	private static final Pattern bukkitPlus = Pattern.compile("^(?:(?:\\[[^\\]]*\\]|(?:([^\\w\\s]?)([^\\w\\s])(?:(?!\\2).)+\\2\\1))\\s*)*(\\w{2,16})(?:\\s*\\([^\\)]*\\))?:\\s*(.*)"); // name is 3

	/*
	 * catches:
	 * *name* text
	 * *name* (staff) text
	 * [rank] -name- text
	 * %^rank^% *name* text
	 * [rank] $%world%$ =name= (staff) text
	 * etc.  basically last word bracketed by matched symbols, which can have a round bracketed word after it and any number of square bracket or balanced symbol (1 or 2) surrounded words before it
	 * examples: blue.realmc.net
	 */
	private static final Pattern matchedSymbolsPlus = Pattern.compile("^(?:(?:\\[[^\\]]*\\]|(?:([^\\w\\s]?)([^\\w\\s])(?:(?!\\2).)+\\2\\1))\\s*)*([\\W&&\\S])(\\w{2,16})\\3+(?:\\s*\\([^\\)]*\\))?\\s*(.*)"); // name is 4

	/*
	 * catches:
	 * [faction] name: text
	 * rank [faction] name: text
	 * *rank [faction] name: text
	 * **rank [faction] name: text
	 * frank f[faction] name: text
	 * c*rank c[faction] name: text
	 * b**rank b[faction] name: text
	 * etc.  basically last word before : or >, preceded by possible square bracketed word and at most one regular word, prepended by 0 to 2 stars, where the stars and the square bracketed word may have a single matching letter before them
	 * examples: some Factions servers
	 */
	private static final Pattern factionsPlus = Pattern.compile("^([^\\*]?)[\\*]*\\w*\\s*(?:\\1\\[[^\\]]*\\])?[\\s]*(\\w{2,16})(?::|(?:\\s*>))\\s*(.*)"); // name is 1
	
	/*
	 * catches:
	 * last non round bracket surrounded word before : or > 
	 * fallback.  more inclusive than the stuff above.  Should get just about everything
	 */
	private static final Pattern fallbackPattern = Pattern.compile("(?:[^:]*[^:\\w])?(\\w{2,16})(?:\\s*\\([^\\)]*\\))?(?::|(?:\\s*>))\\s*(.*)"); // name is 1
	
	/**
	 * True if the chat proxy has been registered, this only happens once the first subscriber registers itself
	 */
	private static boolean registered = false;
	
	/**
	 * Minecraft instance
	 */
	private Minecraft minecraft; 
	
	/**
	 * Thread locking object for access to the subscriber list
	 */
	private Object subscriberLock = new Object();
	
	/**
	 * Subscriber list
	 */
	private ArrayList<IChatSubscriber> chatSubscribers = new ArrayList<IChatSubscriber>();
	
	/**
	 * Last player name guess
	 */
	private String lastChatPlayerName;
	
	/**
	 * Last player entity
	 */
	private EntityPlayer lastChatPlayer;
	
	/**
	 * Constructor
	 * 
	 * @param minecraft
	 */
	protected ChatManager(Minecraft minecraft)
	{
		this.minecraft = minecraft;
	}
	
	@Override
	public void subscribe(IChatSubscriber subscriber)
	{
		synchronized (this.subscriberLock)
		{
			if (!this.chatSubscribers.contains(subscriber))
				this.chatSubscribers.add(subscriber);
			
			if (!registered)
			{
				VoxelPacketListener.registerPacketHandler(this);
				registered = true;
			}
		}
	}
	
	@Override
	public void unsubscribe(IChatSubscriber subscriber)
	{
		synchronized (this.subscriberLock)
		{
			this.chatSubscribers.remove(subscriber);
		}
	}
	
	@Override
	public boolean handleChatPacket(S02PacketChat packet, String message)
	{
		boolean cancel = false;
		
		// Attempt to guess the player that sent the message
		String chatMessage = this.guessPlayer(message);
		
		if (this.lastChatPlayerName != null) { // don't bother if we don't find anyone
			synchronized (this.subscriberLock)
			{
				for (IChatSubscriber subscriber : this.chatSubscribers)
				{
					cancel |= subscriber.receiveChatMessage(message, chatMessage, this.lastChatPlayer, this.lastChatPlayerName);
				}
			}		
		}
		
		return cancel;
	}
	
	/**
	 * Attempts to make an approximate guess at the player name by analyzing the chat message and recent chat messages
	 * 
	 *  
	 * @param message Unparsed chat message
	 * @return message with the guessed player's name removed
	 */
	protected String guessPlayer(String message)
	{
//		String guess = null;
		String cleanMessage = Util.stripColourCodes(message);

		// If the message is likely to be a follow-on, and we sucessfully guessed the player 
		// not really necessary, the name is included with every line as long as we get it before Minecraft's ChatGui breaks it up
		//if (followOnLikely && lastChatPlayerName != null && System.currentTimeMillis() < lastChatPlayerTime + guessLifeSpan)
		//{
		//	guess = lastChatPlayerName;
		//}
		//else
		//{
		// Previous guess has expired
		this.lastChatPlayerName = null;
		this.lastChatPlayer     = null;
		//}

		// Search for vanilla patterns first (not especially likely but worth a go all the same
		Matcher matcher = vanillaPlus.matcher(cleanMessage);
		if (matcher.find()) {
			this.lastChatPlayerName = matcher.group(1);
			this.lastChatPlayer = this.findGuessedPlayer(this.lastChatPlayerName);
			return matcher.group(2);
		}
		matcher = bukkitPlus.matcher(cleanMessage);
		if (matcher.find()) {
			this.lastChatPlayerName = matcher.group(3);
			this.lastChatPlayer = this.findGuessedPlayer(this.lastChatPlayerName);
			return matcher.group(4);
		}
		matcher = matchedSymbolsPlus.matcher(cleanMessage);
		if (matcher.find()) {
			this.lastChatPlayerName = matcher.group(4);
			this.lastChatPlayer = this.findGuessedPlayer(this.lastChatPlayerName);
			return matcher.group(5);
		}
		matcher = factionsPlus.matcher(cleanMessage);
		if (matcher.find()) {
			this.lastChatPlayerName = matcher.group(1);
			this.lastChatPlayer = this.findGuessedPlayer(this.lastChatPlayerName);
			return matcher.group(2);
		}
		matcher = fallbackPattern.matcher(cleanMessage);
		if (matcher.find()) {
			this.lastChatPlayerName = matcher.group(1);
			this.lastChatPlayer = this.findGuessedPlayer(this.lastChatPlayerName);
			return matcher.group(2);
		}
		return "";
	}
	
	/**
	 * Having guessed a player, attempts to find a matching local entity
	 * 
	 * @param guess
	 * @return
	 */
	protected EntityPlayer findGuessedPlayer(String guess)
	{
		if (this.minecraft.theWorld != null && this.minecraft.theWorld.playerEntities != null)
		{
			for (Object entity : this.minecraft.theWorld.playerEntities)
			{
				EntityPlayer playerEntity = (EntityPlayer)entity;
				String playerName = playerEntity.getCommandSenderName();
				
				if (playerName != null && playerName.equalsIgnoreCase(guess))
				{
					this.lastChatPlayerName = playerName;
					return playerEntity;
				}
			}
		}
		
		return null;
	}
}
