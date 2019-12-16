package listener.main;

import java.util.ArrayList;

public class VarTable {
	private ArrayList<element> tempVar;
	private ArrayList<element> localVar;
	private int startNum;
	private int pointer;
	
	class element {
		String varName;
		String varValue;
		String varType;
		Boolean check;
		
		public element(String varName, String varType) {
			this.varName = varName;
			this.varValue = "";
			this.varType = varType;
			this.check = true;
		}
		
		public element(String varName, String varValue, String varType) {
			this.varName = varName;
			this.varValue = varValue;
			this.varType = varType;
			this.check = true;
		}
	}
	
	public VarTable() {
		tempVar = new ArrayList<>();
		localVar = new ArrayList<>();
		startNum = 1779;
		pointer = 0;
	}
	
	public int tempVarSize() {
		return tempVar.size();
	}
	
	public int localVarSize() {
		return localVar.size();
	}
	
	public int returnPointer() {
		return pointer;
	}
	
	public String returnIndexOfTempVar(int i) {
		element element  = tempVar.get(i);
		if(element.check==true) {
			tempVar.get(i).check = false;
			return element.varType + " "+ element.varName +";";
		}
		return "";
	}
	
	public String returnIndexOfLocalVar(int i) {
		element element  = localVar.get(i);
		if(element.check==true) {
			localVar.get(i).check = false;
			return element.varType + " "+ element.varName +";";
		}
		return "";
	}
	
	public String returnStringOfTempVar(String str) {
		element element;
		for(int i = 0; i < tempVar.size(); i++) {
			if(tempVar.get(i).varName.equals(str)) {
				tempVar.get(i).check = false;
				element = tempVar.get(i);
				return element.varName + " = "+ element.varValue +";";
			}
				
		}
		return "NULL";
	}
	
	public String returnIndexOfTempVarData(int i) {
		element element  = tempVar.get(i);
		if(element.check==true) {
			tempVar.get(i).check=false;
			return element.varName+" = "+ element.varValue +";";
		}
		else
			return "";
	}
	
	public String presentTempVar() {
		return "D."+String.valueOf(startNum);
	}
	
	public void addLocalVar(String key, String value) {
		localVar.add(new element(key,value));
	}
	
	public void addTempVar(String value) {
		String newTempVar = "D."+String.valueOf(startNum++);
		tempVar.add(new element(newTempVar,value,"int"));
		pointer=tempVar.size();
	}
	
	public String findValueInLocalVar(String str) {
		for(int i = 0; i < localVar.size(); i++) {
			if(localVar.get(i).varValue.equals(str))
				return localVar.get(i).varName;
		}
		return str;
	}
	
	public String findValueInTempVarByVarValue(String str) {
		for(int i = 0; i < tempVar.size(); i++) {
			if(tempVar.get(i).varValue.equals(str))
				return tempVar.get(i).varName;
		}
		return str;
	}
	
	public String findValueInTempVarByVarName(String str) {
		for(int i = 0; i < tempVar.size(); i++) {
			if(tempVar.get(i).varName.equals(str))
				return tempVar.get(i).varValue;
		}
		return str;
	}
	
	public String printPointer() {
		if(pointer==0)
			return "";
		element element  = tempVar.get(--pointer);
		if(element.check==true) {
			element.check=false;
			return element.varName+" = "+ element.varValue +";";
		}
		else
			return "";
	}
	
	public String FindTempVar(String str) {
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i)=='D') {
				return str.substring(i,i+6);
			}
		}
		return "";
	}

}
