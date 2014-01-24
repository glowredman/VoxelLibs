package com.thevoxelbox.common.util.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import com.thevoxelbox.common.util.PrivateFields;

/**
 * 'Dynamic' text field which supports resizing and moving and also syntax highlight
 *
 * @author Adam Mummery-Smith
 */
public class GuiTextFieldEx extends GuiTextField
{
	/**
	 * Width member is private in the superclass
	 */
	protected int xPos, yPos, width, height;
	
	/**
	 * Allowed character filter for this text box
	 */
	public String allowedCharacters;
	
	public int minStringLength = 0;
	
	protected FontRenderer fontRenderer;
	
	/**
	 * Constructor
	 * 
	 * @param parentScreen Parent screen
	 * @param fontrenderer Font renderer
	 * @param xPos X location
	 * @param yPos Y location
	 * @param width Control width
	 * @param height Control height
	 * @param initialText Text to initially set
	 */
	public GuiTextFieldEx(FontRenderer fontrenderer, int xPos, int yPos, int width, int height, String initialText, String allowedCharacters, int maxStringLength)
	{
		super(fontrenderer, xPos, yPos, width, height);
		this.allowedCharacters = allowedCharacters;
		this.func_146203_f(maxStringLength); // setMaxStringLength
		this.setText(initialText);

		this.width = width;
	}
	
	/**
	 * Constructor
	 * 
	 * @param parentScreen Parent screen
	 * @param fontrenderer Font renderer
	 * @param xPos X location
	 * @param yPos Y location
	 * @param width Control width
	 * @param height Control height
	 * @param initialText Text to initially set
	 */
	public GuiTextFieldEx(FontRenderer fontrenderer, int xPos, int yPos, int width, int height, String initialText)
	{
		super(fontrenderer, xPos, yPos, width, height);
		this.func_146203_f(65536); // setMaxStringLength
		this.setText(initialText);

		this.width = width;
	}
	
	/**
	 * Constructor
	 * 
	 * @param parentScreen Parent screen
	 * @param fontrenderer Font renderer
	 * @param xPos X location
	 * @param yPos Y location
	 * @param width Control width
	 * @param height Control height
	 * @param initialText Text to initially set
	 */
	public GuiTextFieldEx(FontRenderer fontrenderer, int xPos, int yPos, int width, int height, int initialValue, int digits)
	{
		super(fontrenderer, xPos, yPos, width, height);
		this.func_146203_f(digits); // setMaxStringLength
		this.setText(String.valueOf(initialValue));
		this.allowedCharacters = "0123456789";
		this.width = width;
	}
	
	@Override
	public boolean textboxKeyTyped(char keyChar, int keyCode)
	{
		if ((this.allowedCharacters == null || this.allowedCharacters.indexOf(keyChar) >= 0) ||
				keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT ||
				keyCode == Keyboard.KEY_HOME || keyCode == Keyboard.KEY_END ||
				keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK)
		{
			return super.textboxKeyTyped(keyChar, keyCode);
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.src.GuiTextField#func_50038_e()
	 */
	@Override
	public void func_146202_e() // setCursorPositionEnd
	{
		try
		{
			super.func_146202_e(); // setCursorPositionEnd
		}
		catch (Exception ex) { }
	}

	public void setSizeAndPosition(int xPos, int yPos, int width, int height)
	{
		this.xPos   = PrivateFields.textFieldXPos.set(this, xPos);
		this.yPos   = PrivateFields.textFieldYPos.set(this ,yPos);
		this.width  = PrivateFields.textFieldWidth.set(this, width);
		this.height = PrivateFields.textFieldHeight.set(this, height);
	}

	public void setSize(int width, int height)
	{
		this.width  = PrivateFields.textFieldWidth.set(this, width);
		this.height = PrivateFields.textFieldHeight.set(this, height);
	}

	public void setPosition(int xPos, int yPos)
	{
		this.xPos   = PrivateFields.textFieldXPos.set(this, xPos);
		this.yPos   = PrivateFields.textFieldYPos.set(this ,yPos);
	}
	
	public void scrollToEnd()
	{
		this.func_146190_e(0);
		this.func_146190_e(this.getText().length());
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.src.GuiTextField#func_50032_g(int)
	 */
	@Override
	public void func_146190_e(int cursorPos) // setCursorPosition
	{
		super.func_146190_e(cursorPos); // setCursorPosition
		super.func_146190_e(cursorPos); // setCursorPosition
	}

	/**
	 * Synchronise private members from the superclass using reflection
	 */
	protected void syncMembers()
	{
		this.xPos   = PrivateFields.textFieldXPos.get(this);
		this.yPos   = PrivateFields.textFieldYPos.get(this);
		this.width  = PrivateFields.textFieldWidth.get(this);
		this.height = PrivateFields.textFieldHeight.get(this);
	}

	public void drawTextBoxAt(int yPos)
	{
		try
		{
			PrivateFields.textFieldYPos.set(this, yPos);
			this.drawTextBox();
		}
		catch (Exception ex) {}
	}

	public int getIntValue(int defaultValue)
	{
		try
		{
			return Integer.parseInt(this.getText());
		}
		catch (Exception ex) { return defaultValue; }
	}
}
