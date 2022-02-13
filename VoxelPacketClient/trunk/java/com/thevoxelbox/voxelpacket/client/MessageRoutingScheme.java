package com.thevoxelbox.voxelpacket.client;

/**
 * Defines a message routing scheme for an outbound message.
 * 
 * @author Adam Mummery-Smith
 *
 */
public enum MessageRoutingScheme
{
	/**
	 * Route the message to local clients and send it to the server
	 */
	Both,
	
	/**
	 * Route the message to local subscribers only
	 */
	Local,
	
	/**
	 * Route the message to the server only
	 */
	Remote;
	
	/**
	 * 
	 * @return
	 */
	public boolean routeLocally()
	{
		return this.equals(MessageRoutingScheme.Both) || this.equals(MessageRoutingScheme.Local);
	}

	/**
	 * 
	 * @return
	 */
	public boolean routeRemotely()
	{
		return this.equals(MessageRoutingScheme.Both) || this.equals(MessageRoutingScheme.Remote);
	}
}
