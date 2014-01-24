package com.thevoxelbox.voxelpacket.common.struct;

import java.io.Serializable;

/**
 * Data structure for responding to server->client queries over the channel
 * 
 * @author Adam Mummery-Smith
 *
 */
public class QueryResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8315618453016340417L;

	/**
	 * Query code this response applies to
	 */
	public String responseCode;
	
	/**
	 * Response data to the query
	 */
	public Object responseData;
	
	/**
	 * Private constructor used during deserialisation
	 */
	@SuppressWarnings("unused")
	private QueryResponse() {}
	
	/**
	 * 
	 * @param responseCode Query code this response applies to 
	 * @param responseData Response data to the query
	 */
	public QueryResponse(String responseCode, Object responseData)
	{
		this.responseCode = responseCode;
		this.responseData = responseData;
	}
	
	/**
	 * Generic accessor for the response data
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T data()
	{
		return (T) this.responseData;
	}
}
