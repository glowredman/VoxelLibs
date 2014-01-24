package com.thevoxelbox.common.util.upload.awt;

import javax.swing.JFileChooser;

/**
 * Interface for objects which can receive a callback from ThreadOpenFile
 *
 * @author Adam Mummery-Smith
 */
public interface IOpenFileCallback
{
	
	/**
	 * Callback method called when the "open file" dialog is closed
	 * 
	 * @param fileDialog
	 * @param dialogResult
	 */
	public abstract void onFileOpenDialogClosed(JFileChooser fileDialog, int dialogResult);
	
}