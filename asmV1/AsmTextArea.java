package asmV1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AsmTextArea extends JTextPane {
	private static final long serialVersionUID = 7463647123072166580L;
	Color command_color=Color.red;
	Color register_color=Color.blue;
	Color comment_color=Color.magenta;
	Color lineNum_color=Color.gray;
	Color errorBack_color=Color.red;
	Color translator_color=Color.getHSBColor(1.4f,1.4f,1.3f);
	private String translatorPattern="(nop|halt|\\.fill|\\.space)";
	
	private boolean newStyle=false;
	
	public void setStyle(boolean style)
	{
		newStyle=style;
		if (newStyle)
			translatorPattern="(nop|halt|\\.fill|\\.space|\\.org)";
		else
			translatorPattern="(nop|halt|\\.fill|\\.space)";
		
		applyColors();
	}
	
	public AsmTextArea(){
		super();
		setPreferredSize(new Dimension(300,220));
		setMinimumSize(new Dimension(300,220));
	}
	
	
	public void highlightCommands() {
		   StyledDocument doc = getStyledDocument();
		   Pattern pattern= Pattern.compile("(addi|add|nand|lui|sw|lw|beq|jalr)",Pattern.CASE_INSENSITIVE);
		   try {
			   String text = doc.getText(0,doc.getLength());
		   
			   Matcher matcher = pattern.matcher(text);
		   
			   MutableAttributeSet mas = new SimpleAttributeSet();
			   StyleConstants.setForeground(mas, command_color);
	      // StyleConstants.setBackground(mas, Color.white);
			   while(matcher.find()){
				   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
			   }
		   } catch (BadLocationException e) {
				e.printStackTrace();
		}
    }
	
	private void highlightRegisters() {
		   StyledDocument doc = getStyledDocument();
		   Pattern pattern= Pattern.compile("r[0-9]",Pattern.CASE_INSENSITIVE);
		   try {
			   String text = doc.getText(0,doc.getLength());
			   Matcher matcher = pattern.matcher(text);
		   
			   MutableAttributeSet mas = new SimpleAttributeSet();
			   StyleConstants.setForeground(mas, register_color);
	      // StyleConstants.setBackground(mas, Color.white);
			   while(matcher.find()){
				   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
			   }
		   } catch (BadLocationException e) {
				e.printStackTrace();
		   }
    }
	
	private void highlightComments() {
		StyledDocument doc = getStyledDocument();
		   Pattern pattern= Pattern.compile("(#|//).*",Pattern.CASE_INSENSITIVE);
		try {
			String text = doc.getText(0,doc.getLength());
		   Matcher matcher = pattern.matcher(text);
		   
		   MutableAttributeSet mas = new SimpleAttributeSet();
		   StyleConstants.setForeground(mas, comment_color);
		   while(matcher.find()){
			   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
           }
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
	
	private void highlightLineNumbers() {
		StyledDocument doc = getStyledDocument();
		   Pattern pattern= Pattern.compile("(^|\n) *[0-9A-F]{4}:",Pattern.CASE_INSENSITIVE);
		try {
			String text = doc.getText(0,doc.getLength());
		   Matcher matcher = pattern.matcher(text);
		   
		   MutableAttributeSet mas = new SimpleAttributeSet();
		   StyleConstants.setForeground(mas, lineNum_color);
		   while(matcher.find()){
			   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
           }
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
	
	public void highlightError(String what) {
		StyledDocument doc = getStyledDocument();
		   Pattern pattern= Pattern.compile(what,Pattern.CASE_INSENSITIVE);
		try {
			String text = doc.getText(0,doc.getLength());
		   Matcher matcher = pattern.matcher(text);
		   
		   MutableAttributeSet mas = new SimpleAttributeSet();
		   StyleConstants.setBackground(mas, errorBack_color);
		   while(matcher.find()){
			   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
           }
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
	
	private void highlightTranslatorCommands() {
		StyledDocument doc = getStyledDocument();
		
		Pattern pattern= Pattern.compile(translatorPattern,Pattern.CASE_INSENSITIVE);
			
		try {
			String text = doc.getText(0,doc.getLength());
		   Matcher matcher = pattern.matcher(text);
		   
		   MutableAttributeSet mas = new SimpleAttributeSet();
		   StyleConstants.setForeground(mas, translator_color);
		   while(matcher.find()){
			   doc.setCharacterAttributes(matcher.start(),matcher.end()-matcher.start(),mas,true);
           }
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
	
	private void removeStyles(){
		StyledDocument doc = getStyledDocument();
		MutableAttributeSet mas = new SimpleAttributeSet();
		StyleConstants.setForeground(mas, Color.black);
	    StyleConstants.setBackground(mas, Color.white);
	    doc.setCharacterAttributes(0,getText().length() ,mas,true);
		
	}
	
	public void applyColors(){
		removeStyles();
   		highlightLineNumbers();
   		highlightTranslatorCommands();
   		highlightCommands();
   		highlightRegisters();
   		highlightComments();
	}
	
	public void setText(String text){
		super.setText(text);
		applyColors();
	}
    
    protected void processKeyEvent(KeyEvent e) {
   		super.processKeyEvent(e);  	
    	applyColors();
    }
    
}
