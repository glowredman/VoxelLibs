package com.thevoxelbox.common.util.upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Uploader for Multipart form data
 *
 * @author Adam Mummery-Smith
 */
public class ThreadMultipartPostUpload extends Thread
{
	protected final Map<String, Object> sourceData;
	
	protected final String method;
	
	protected final String authorization;
	
	protected final String urlString;
	
	protected final IUploadCompleteCallback callback;
	
	protected HttpURLConnection httpClient;
	
	protected static final String CRLF = "\r\n";
	
	protected static final String twoHyphens = "--";
	
	protected static final String boundary = "----------AaB03x";
	
	public String response;
	
	public int httpResponseCode;
	
	public String httpResponse;
	
	public ThreadMultipartPostUpload(String method, String url, Map<String, Object> sourceData, String authorization, IUploadCompleteCallback callback)
	{
		this.method = method;
		this.urlString = url;
		this.sourceData = sourceData;
		this.authorization = authorization;
		this.callback = callback;
	}
	
	public ThreadMultipartPostUpload(String url, Map<String, Object> sourceData, IUploadCompleteCallback callback)
	{
		this("POST", url, sourceData, null, callback);
	}
	
	public String getResponse()
	{
		return this.response == null ? "" : this.response.trim();
	}
	
	@Override
	public void run()
	{
		try
		{
			this.uploadMultipart();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();

			try
			{
				this.httpResponseCode = this.httpClient.getResponseCode();
				this.httpResponse = this.httpClient.getResponseMessage();
			}
			catch (Exception ex1)
			{
			}
		}
		
		this.callback.onUploadComplete(this.getResponse());
	}
	
	protected void uploadMultipart() throws IOException
	{
		// open a URL connection
		URL url = new URL(this.urlString);
		
		// Open a HTTP connection to the URL
		this.httpClient = (HttpURLConnection)url.openConnection();
		this.httpClient.setDoOutput(true);
		this.httpClient.setUseCaches(false);
		
		this.httpClient.setRequestMethod(this.method);
		this.httpClient.setRequestProperty("Connection", "Close");
		this.httpClient.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"); // For CloudFlare
		
		if (this.sourceData.size() > 0)
		{
			this.httpClient.setRequestProperty("Content-Type", "multipart/form-data, boundary=" + ThreadMultipartPostUpload.boundary);
		}
		
		if (this.authorization != null)
		{
			this.httpClient.addRequestProperty("Authorization", this.authorization);
		}
		
		DataOutputStream outputStream = new DataOutputStream(this.httpClient.getOutputStream());
		
		for (Entry<String, Object> data : this.sourceData.entrySet())
		{
			outputStream.writeBytes(ThreadMultipartPostUpload.twoHyphens + ThreadMultipartPostUpload.boundary + ThreadMultipartPostUpload.CRLF);
			
			String paramName = data.getKey();
			Object paramData = data.getValue();
			
			if (paramData instanceof File)
			{
				File uploadFile = (File)paramData;
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + uploadFile.getName() + "\"" + ThreadMultipartPostUpload.CRLF + ThreadMultipartPostUpload.CRLF);
				this.writeFile(uploadFile, outputStream);
			}
			else
			{
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"" + ThreadMultipartPostUpload.CRLF + ThreadMultipartPostUpload.CRLF);
				outputStream.writeBytes(paramData.toString());
			}
			
			outputStream.writeBytes(ThreadMultipartPostUpload.CRLF);
		}
		
		outputStream.writeBytes(ThreadMultipartPostUpload.twoHyphens + ThreadMultipartPostUpload.boundary + ThreadMultipartPostUpload.twoHyphens + ThreadMultipartPostUpload.CRLF);
		outputStream.flush();
		
		InputStream httpStream = this.httpClient.getInputStream();
		
		try
		{
			StringBuilder readString = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpStream));

			String readLine;
			while ((readLine = reader.readLine()) != null)
			{
				readString.append(readLine).append("\n");
			}
			
			reader.close();
			this.response = readString.toString();
		}
		catch (IOException ex) { ex.printStackTrace(); }
		
		outputStream.close();

		this.httpResponseCode = this.httpClient.getResponseCode();
		this.httpResponse = this.httpClient.getResponseMessage();
	}

	/**
	 * @param sourceFile
	 * @param outputStream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeFile(File sourceFile, DataOutputStream outputStream) throws FileNotFoundException, IOException
	{
		int bytesRead, bufferSize;
		int maxBufferSize = 1 * 1024 * 1024;
		
		FileInputStream fileInputStream = new FileInputStream(sourceFile);
		
		int bytesAvailable = fileInputStream.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		byte[] buffer = new byte[bufferSize];
		
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
		while (bytesRead > 0)
		{
			outputStream.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}
		
		fileInputStream.close();
	}
}
