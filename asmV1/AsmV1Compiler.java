package asmV1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsmV1Compiler {
	public int errno=0;
	public int out=0;
	public int lineno=0;
	public int mempoint=0; // needed for the .org command
	private boolean newStyle=false; 
	public final int ERR_NOT_FOUND_LINE_NUMBER=-1;
	public final int ERR_BAD_SYNTAX=-2;
	public final int SPACE_COMM=1;
	public final int IGNORE_LINE=2;
	
	public void setStyle(boolean inp)
	{
		newStyle=inp;
	}
	
	public boolean getStyle()
	{
		return newStyle;
	}
	
	public void zeroMempoint()
	{
		mempoint=0;
	}
	
	public void setMempoint(int inp)
	{
		mempoint=inp;
	}
	
	public void incMempoint()
	{
		mempoint++;
	}
		
	public int processCommand(String cmd){
		out=0;errno=0;
		//getting line number
		Pattern pattern= Pattern.compile("^\\s*([0-9A-F]{4}):?(.*)",Pattern.CASE_INSENSITIVE);
		Pattern fillPat= Pattern.compile("^\\s*(\\.fill)\\s+(0x[0-9A-F]{1,4})\\s*",Pattern.CASE_INSENSITIVE);
		Pattern spacePat= Pattern.compile("^\\s*(\\.space)\\s+(0x[0-9A-F]{1,4})\\s*",Pattern.CASE_INSENSITIVE);		
		//for the newStyle we need an .org command
		Pattern orgPat= Pattern.compile("^\\s*(\\.org)\\s+(0x[0-9A-F]{1,4})\\s*",Pattern.CASE_INSENSITIVE);
		
		
		Matcher matcher = pattern.matcher(cmd);
		
		if(!matcher.matches()&!newStyle) {
			errno=ERR_NOT_FOUND_LINE_NUMBER;
			return -1;
		}else{
			if(newStyle)
			{
				lineno=mempoint;
				System.out.println("Mempoint is: "+mempoint);
			}
			else
			{
				lineno=Integer.parseInt(matcher.group(1),16);
			}
			
			String command="";
			if(newStyle)
				command = cmd;
			else
				command = matcher.group(2);
						
			
			command=command.toLowerCase();
			command=command.trim();
			if(command.equals("nop")) {
			//command="add r0,r0,r0";
				out=0;
				errno=0;
				return out;
			}
			if(command.equals("halt")){
				out=0xE07F;
				errno=0;
				return out;
			}
			matcher=fillPat.matcher(command);
			if(matcher.matches()){
				out=getHexOrDecNumber(matcher.group(2));
				errno=0;
				if(out>0xFFFF)
					BadLine();
				return out;
			}			
			matcher=spacePat.matcher(command);
			if(matcher.matches()){
				out=getHexOrDecNumber(matcher.group(2));
				errno=SPACE_COMM;
				if(out>0xFFFF)
					BadLine();
				return out;
			}	
			matcher=orgPat.matcher(command);
			if(matcher.matches()&newStyle)
			{
				out=getHexOrDecNumber(matcher.group(2));
				errno=IGNORE_LINE;
				mempoint=out;
				if(out>0xFFFF)
					BadLine();
				return out;
			}
			if(out==0)
				compile(command);
			
			if(newStyle && errno==0) incMempoint();
			return out;
		}
	}
	
	private void compile(String command) {
		//System.out.println("Got: "+command);
		command=command.replaceAll("(#|//).*","");
		if(command.equals("")){
			errno=IGNORE_LINE;
			out=0;
			return;
		}
		//System.out.println("NoComments: "+command);
		
		Pattern type1= Pattern.compile("^\\s*(add|nand)\\s+r([0-7])\\s*,\\s*r([0-7]),\\s*r([0-7])\\s*$",Pattern.CASE_INSENSITIVE);
		Pattern type2= Pattern.compile("^\\s*(addi|lw|sw|beq)\\s+r([0-7])\\s*,\\s*r([0-7]),\\s*((?:-?[0-9]{1,2})|(?:0x[0-9A-F]{1,2}))\\s*$",Pattern.CASE_INSENSITIVE);
		Pattern type3= Pattern.compile("^\\s*(lui)\\s+r([0-7])\\s*,\\s*((?:-?[0-9]{1,4})|(?:0x[0-9A-F]{1,3}))\\s*$",Pattern.CASE_INSENSITIVE);
		Pattern jalrType= Pattern.compile("^\\s(jalr)\\s+r([0-7])\\s*,\\s*r([0-7])\\s*$",Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = type1.matcher(command);
		if(matcher.matches()){
			String cmd=matcher.group(1);
			int regA=Integer.parseInt(matcher.group(2));
			int regB=Integer.parseInt(matcher.group(3));
			int regC=Integer.parseInt(matcher.group(4));
			System.out.println(cmd+" "+regA+" "+regB+" "+regC);
			//assembling all reg
			out=(regA<<10)|(regB<<7)|regC;
			//add not needed
			if(cmd.equals("nand")) out=out|(2<<13);
		}else{
			matcher = type2.matcher(command);
			if(matcher.matches()){
				String cmd=matcher.group(1);
				int regA=Integer.parseInt(matcher.group(2));
				int regB=Integer.parseInt(matcher.group(3));
				String num=matcher.group(4);
				System.out.println(cmd+" "+regA+" "+regB+" "+num);
			
				int data;
				data=getHexOrDecNumber(num);
				
				if(data<-64 || data>63) BadLine();
				out=(regA<<10)|(regB<<7)|(data&0x7F);
				if(cmd.equals("addi")) out=out|(1<<13);
				if(cmd.equals("lw")) out=out|(4<<13);
				if(cmd.equals("sw")) out=out|(5<<13);
				if(cmd.equals("beq")) out=out|(6<<13);
			}else{
				matcher = type3.matcher(command);
				if(matcher.matches()){
					//String cmd=matcher.group(1);
					int regA=Integer.parseInt(matcher.group(2));
					String num=matcher.group(3);
					
					int data;
					data=getHexOrDecNumber(num);
					if(data>0x3FF) BadLine();
					out=(3<<13)|(regA<<10)|(data&0x3FF);//only lui is possible
					//System.out.println(cmd+" "+regA+" "+num);
				}else{
					matcher = jalrType.matcher(command);
					if(matcher.matches()){
						//String cmd=matcher.group(1);
						int regA=Integer.parseInt(matcher.group(2));
						int regB=Integer.parseInt(matcher.group(3));
						//System.out.println(cmd+" "+regA+" "+regB);
						out=(7<<13)|(regA<<10)|(regB<<7);
					}else BadLine();
				}
			}
		}
		
	}
	
	private void BadLine(){
		out=-1;
		errno=ERR_BAD_SYNTAX;
	}
	
	private int getHexOrDecNumber(String num){
		int out;
		if(num.length()>2 && num.charAt(1)=='x'){
			out=Integer.parseInt(num.substring(2),16);
		}else out=Integer.parseInt(num,10);
		return out;
	}

}
