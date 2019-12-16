package listener.main;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import generated.MiniCBaseListener;
import generated.MiniCParser;
import generated.MiniCParser.ParamsContext;
import listener.main.SymbolTable.Type;

public class GCCGIMPLEGenListener extends MiniCBaseListener implements ParseTreeListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>(); // children�� ������ ParseTreeProperty
	VarTable varTable = new VarTable();
	private int depth = 0; // nesting depth

	@Override
	public void enterProgram(MiniCParser.ProgramContext ctx) {
	}

	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) // getChildCount = del+�� ���̹Ƿ� del�� ����ŭ ���
			System.out.println(newTexts.get(ctx.getChild(i)));
	}

	@Override
	public void enterDecl(MiniCParser.DeclContext ctx) {

	}

	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		newTexts.put(ctx, newTexts.get(ctx.getChild(0))); // Decl ���� �� newTexts�� 0��° �ڽ��� ����
	}

	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {

	}

	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String s1, id, s2, lt, s3, semi;
		if (ctx.getChildCount() == 3) { // type_spec IDENT ';'
			s1 = newTexts.get(ctx.getChild(0));
			id = ctx.getChild(1).getText();
			semi = ctx.getChild(2).getText();
			newTexts.put(ctx, s1 + " " + id + semi); // newTexts�� ����
		} else if (ctx.getChildCount() == 5) { // type_spec IDENT '=' LITERAL ';'
			s1 = newTexts.get(ctx.getChild(0));
			id = ctx.getChild(1).getText();
			s2 = ctx.getChild(2).getText();
			lt = ctx.getChild(3).getText();
			semi = ctx.getChild(4).getText();
			newTexts.put(ctx, s1 + " " + id + " " + s2 + " " + lt + semi); // newTexts�� ����
		} else if (ctx.getChildCount() == 6) { // type_spec IDENT '[' LITERAL ']' ';'
			s1 = newTexts.get(ctx.getChild(0));
			id = ctx.getChild(1).getText();
			s2 = ctx.getChild(2).getText();
			lt = ctx.getChild(3).getText();
			s3 = ctx.getChild(4).getText();
			semi = ctx.getChild(5).getText();
			newTexts.put(ctx, s1 + " " + id + s2 + lt + s3 + semi); // newTexts�� ����
		}
	}

	@Override
	public void enterType_spec(MiniCParser.Type_specContext ctx) {

	}

	@Override
	public void exitType_spec(MiniCParser.Type_specContext ctx) {
		newTexts.put(ctx, ctx.getChild(0).getText()); // �Է��� VOID�� INT �̹Ƿ� �ٷ� newTexts�� ����
	}

	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		// type_spec IDENT '(' params ')' compound_stmt
		String s1, s2, L_paren, s4, R_paren, s6;
//		s1 = newTexts.get(ctx.getChild(0));
		s2 = ctx.getChild(1).getText();
		L_paren = ctx.getChild(2).getText();
		s4 = newTexts.get(ctx.getChild(3));
		R_paren = ctx.getChild(4).getText();
		s6 = newTexts.get(ctx.getChild(5));
		newTexts.put(ctx, s2 + L_paren + s4 + R_paren + "\r\n" + s6);
	}

	@Override
	public void enterParams(MiniCParser.ParamsContext ctx) {

	}

	@Override
	public void exitParams(MiniCParser.ParamsContext ctx) {
		if (ctx.children == null) {
			// ��ĭ
			newTexts.put(ctx, "");
		} else { // param (',' param)* or VOID
			String sum = ""; // ������ ������
			String[] s = new String[ctx.getChildCount()];
			s[0] = newTexts.get(ctx.getChild(0));
			for (int i = 1; i < ctx.getChildCount(); i += 2) {
				s[i] = ctx.getChild(i).getText();
				s[i + 1] = newTexts.get(ctx.getChild(i + 1));
			}
			for (int i = 0; i < ctx.getChildCount(); i++) {
				if (s[i].equals(",")) // ���Ұ� ,���
					sum += s[i] + " "; // ������ �߰�
				else // �� ���� ���Ҷ��
					sum += s[i]; // ������ �߰���������
			}
			newTexts.put(ctx, sum); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterParam(MiniCParser.ParamContext ctx) {

	}

	@Override
	public void exitParam(MiniCParser.ParamContext ctx) {
		String s1, id, s2, s3;
		if (ctx.getChildCount() == 2) { // type_spec IDENT
			s1 = newTexts.get(ctx.getChild(0));
			id = ctx.getChild(1).getText();
			newTexts.put(ctx, s1 + " " + id); // ��� ���� ���� newTexts�� ����
		} else { // type_spec IDENT '[' ']'
			s1 = newTexts.get(ctx.getChild(0));
			id = ctx.getChild(1).getText();
			s2 = ctx.getChild(2).getText();
			s3 = ctx.getChild(3).getText();
			newTexts.put(ctx, s1 + " " + id + s2 + s3); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterStmt(MiniCParser.StmtContext ctx) {

	}

	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		// stmt�� �Է¹����� �� Ÿ�Կ� ���� �з��Ǿ� �ش� �Լ��� ����ǹǷ� ���常 ��
		newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
	}

	@Override
	public void enterExpr_stmt(MiniCParser.Expr_stmtContext ctx) {

	}

	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		// expr ';'
		String s1, semi;
		
		s1 = newTexts.get(ctx.getChild(0)); // expr
		semi = ctx.getChild(1).getText(); // ';'
		newTexts.put(ctx, s1 + semi); // ��� ���� ���� newTexts�� ����
	}

	@Override
	public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {

	}

	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		String s1, L_paren, s2, R_paren, s3;
		// WHILE '(' expr ')' stmt
		String result = "";
		for (int i = 0; i < varTable.returnPointer(); i++) {
			String temp = varTable.returnIndexOfTempVarData(i);
			if( i != 0 && !temp.equals(""))
				result += getNumOfDot(depth);
			if(!temp.equals(""))
				result += temp+"\n";
		}
		s1 = newTexts.get(ctx.getChild(0)); // WHILE
		L_paren = ctx.getChild(1).getText(); // '('
		s2 = newTexts.get(ctx.getChild(2)); // expr
		R_paren = ctx.getChild(3).getText(); // ')'
		s3 = newTexts.get(ctx.getChild(4)); // stmt
		newTexts.put(ctx, result+getNumOfDot(depth)+s1 + " " + L_paren + s2 + R_paren + "\n" + s3);
	}

	@Override
	public void enterCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		depth++; // ���ο� �߰�ȣ �����̹Ƿ� depth 1 ����
	}

	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		// '{' local_decl* stmt* '}'
		String L_brace, s1, R_brace, s2;
		if (ctx.getChildCount() == 2) { // local_decl�̳� stmt�� ��� ���� ���
			L_brace = ctx.getChild(0).getText();
			R_brace = ctx.getChild(1).getText();
			newTexts.put(ctx, L_brace + "\n" + R_brace); // �߰�ȣ�� ����
		} else if (ctx.getChildCount() == 3) { // local_decl�̳� stmt�� �ϳ��� ���� ���
			L_brace = getNumOfDot(depth - 1) + ctx.getChild(0).getText();
			s1 = getNumOfDot(depth) + newTexts.get(ctx.getChild(1));
			R_brace = getNumOfDot(depth - 1) + ctx.getChild(2).getText();
			newTexts.put(ctx, L_brace + "\n" + s1 + "\n" + R_brace);
		} else { // local_decl�̳� stmt�� �ϳ� �̻��� ���
			String[] array = new String[ctx.getChildCount()];
			L_brace = getNumOfDot(depth - 1) + ctx.getChild(0).getText();
			R_brace = getNumOfDot(depth - 1) + ctx.getChild(array.length - 1).getText();
			array[0] = L_brace; // �迭�� ���� ���� ���� �߰�ȣ ����
			for (int i = 1; i < array.length - 1; i++) { // local_decl �Ǵ� stmt ����
				array[i] = getNumOfDot(depth) + newTexts.get(ctx.getChild(i));
			}
			array[array.length - 1] = R_brace; // �迭�� ���� ���� ���� �߰�ȣ ����
			String result = L_brace; // ���� ���� �߰�ȣ �� ����
			for (int i = 0; i < varTable.tempVarSize(); i++)
				result += "\n" + getNumOfDot(depth) + varTable.returnIndexOfTempVar(i);
			for (int i = 0; i < varTable.localVarSize(); i++)
				result += "\n" + getNumOfDot(depth) + varTable.returnIndexOfLocalVar(i);
			for (int i = 1; i < array.length; i++) { // ���� ���� �߰�ȣ �̿��� �� ����
				if(!array[i].equals("\t"))
					result += "\n" + array[i];
			}
			newTexts.put(ctx, result); // ��� ���� ���� newTexts�� ����
		}
		depth--; // �߰�ȣ�� ����Ǿ����Ƿ� depth 1 ����
	}

	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {

	}

	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) { // ��������
		String s1, s2, s3, s4, s5, semi;
		if (ctx.getChildCount() == 3) {
			// type_spec IDENT ';'
			s1 = newTexts.get(ctx.getChild(0));
			s2 = ctx.getChild(1).getText();
			semi = ctx.getChild(2).getText();
			varTable.addLocalVar(s2, s1);
//			newTexts.put(ctx,s2 + semi);
			newTexts.put(ctx, "");
		} else if (ctx.getChildCount() == 5) {
			// type_spec IDENT '=' LITERAL ';'
			s1 = newTexts.get(ctx.getChild(0));
			s2 = ctx.getChild(1).getText();
			varTable.addLocalVar(s2, s1);
			s3 = ctx.getChild(2).getText();
			s4 = ctx.getChild(3).getText();
			semi = ctx.getChild(4).getText();
			newTexts.put(ctx, s2 + " " + s3 + " " + s4 + semi); // ��� ���� ���� newTexts�� ����
		} else if (ctx.getChildCount() == 6) {
			// type_spec IDENT '[' LITERAL ']' ';'
			s1 = newTexts.get(ctx.getChild(0));
			s2 = ctx.getChild(1).getText();
			s3 = ctx.getChild(2).getText();
			s4 = ctx.getChild(3).getText();
			s5 = ctx.getChild(4).getText();
			semi = ctx.getChild(5).getText();
			varTable.addLocalVar(s2+s3+s4+s5, s1);
			newTexts.put(ctx, s1 + " " + s2 + s3 + s4 + s5 + semi); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {

	}

	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String s1, L_paren, exp, R_paren, st, s2, st2;
		
		String result = "";
		for (int i = 0; i < varTable.returnPointer(); i++) {
			String temp = varTable.returnIndexOfTempVarData(i);
			if( i != 0 && !temp.equals(""))
				result += getNumOfDot(depth);
			if(!temp.equals(""))
				result += temp+"\n";
		}

		if (ctx.children.size() == 5) {
			// IF '(' expr ')' stmt
			s1 = ctx.getChild(0).getText();
			L_paren = ctx.getChild(1).getText();
			exp = newTexts.get(ctx.getChild(2));
			R_paren = ctx.getChild(3).getText();
			if (ctx.getChild(4).getChild(0) instanceof MiniCParser.Compound_stmtContext) { // if�� �Է¹޾��� �� �߰�ȣ �Է��� �ִ� ���
				st = newTexts.get(ctx.getChild(4));
			} else { // if�� �Է¹޾��� �� �߰�ȣ �Է��� ���� ���
				st = getNumOfDot(depth) + "{\n" + getNumOfDot(depth + 1) + newTexts.get(ctx.getChild(4)) + "\n"
						+ getNumOfDot(depth) + "}";
			}
			newTexts.put(ctx, result + getNumOfDot(depth) + s1 + " " + L_paren + exp + R_paren + "\n" + st); // ��� ���� ���� newTexts�� ����
		} else {
			// IF '(' expr ')' stmt ELSE stmt
			s1 = newTexts.get(ctx.getChild(0));
			L_paren = ctx.getChild(1).getText();
			exp = newTexts.get(ctx.getChild(2));
			R_paren = ctx.getChild(3).getText();
			st = newTexts.get(ctx.getChild(4));
			s2 = newTexts.get(ctx.getChild(5));
			st2 = newTexts.get(ctx.getChild(6));
			newTexts.put(ctx, result + getNumOfDot(depth) + s1 + " " + "\n" + L_paren + exp + "\n" + R_paren + st + s2 + st2); // ��� ���� ���� newTexts��
																									// ����
		}
	}

	@Override
	public void enterReturn_stmt(MiniCParser.Return_stmtContext ctx) {

	}

	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		String re, exp, semi;
		if (ctx.getChildCount() == 2) {
			// RETURN ';'
			re = newTexts.get(ctx.getChild(0));
			semi = ctx.getChild(1).getText();
			newTexts.put(ctx, re + semi); // ��� ���� ���� newTexts�� ����
		} else {
			// RETURN expr ';'
			re = newTexts.get(ctx.getChild(0));
			exp = newTexts.get(ctx.getChild(1));
			semi = ctx.getChild(2).getText();
			newTexts.put(ctx, re + " " + exp + semi); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterExpr(MiniCParser.ExprContext ctx) {
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String s1 = null, s2 = null, op = null, s3 = null, s4 = null, s5 = null, s6 = null;

		if (ctx.getChildCount() == 1) {
			// LITERLAL || IDENT
			s1 = ctx.getChild(0).getText();
			newTexts.put(ctx, s1); // ��� ���� ���� newTexts�� ����
		} else if (ctx.getChildCount() == 2) {
			// '-' expr || '+' expr || '!' expr
			s1 = ctx.getChild(0).getText();
			s2 = newTexts.get(ctx.getChild(1));
			newTexts.put(ctx, s1 + s2); // ��� ���� ���� newTexts�� ����
		} else if (ctx.getChildCount() == 3) {
			if (isBinaryOperation(ctx)) {
				// expr + expr || expr * expr || expr - expr || expr / expr || expr % expr
				// expr EQ expr || expr NE expr || expr LE expr || expr GE expr || expr AND expr
				// || expr OR expr
				// expr '<' expr || expr '>' expr
				if (ctx.getChild(0).getText().equals("(") && ctx.getChild(2).getText().equals(")")) {
					// '(' expr ')'
					s1 = ctx.getChild(0).getText();
					s2 = newTexts.get(ctx.getChild(1));
					s3 = ctx.getChild(2).getText();
					newTexts.put(ctx, s1 + s2 + s3); // ��� ���� ���� newTexts�� ����
				}
				else {
					s1 = newTexts.get(ctx.expr(0));
					s2 = newTexts.get(ctx.expr(1));
					op = ctx.getChild(1).getText();
					String presentTempVar = varTable.presentTempVar();
					varTable.addTempVar(s1 + " " + op + " " + s2);
					newTexts.put(ctx, presentTempVar); // ��� ���� ���� newTexts�� ����
				}
			} else {
				if (ctx.getChild(0).getText().equals("(") && ctx.getChild(2).getText().equals(")")) {
					// '(' expr ')'
					s1 = ctx.getChild(0).getText();
					s2 = newTexts.get(ctx.getChild(1));
					s3 = ctx.getChild(2).getText();
					newTexts.put(ctx, s1 + s2 + s3); // ��� ���� ���� newTexts�� ����
				} else if (ctx.getChild(1).getText().equals("=")) {
					// IDENT '=' expr
					String result = "";
					String temp1, temp3, t1 , t3;
					s1 = t1 = temp1 = newTexts.get(ctx.getChild(0));
					s2 = ctx.getChild(1).getText();
					s3 = t3 = temp3 = newTexts.get(ctx.getChild(2));
					boolean GoOrStop = true;
					while(GoOrStop) {
						if(t1.contains("D.")|| t3.contains("D.")) {	
							t1 = temp1;
							t3 = temp3;
							temp1 = varTable.findValueInTempVarByVarName(varTable.FindTempVar(t1));
							temp3 = varTable.findValueInTempVarByVarName(varTable.FindTempVar(t3));
							if(temp1==t1 && temp3==t3)
								GoOrStop = false;
							String output = varTable.printPointer();
							if(!output.equals("")) {
								result = getNumOfDot(depth) + result;
								result = output+"\n"+result;
							}
						}
						else
							break;
					}
					if(!result.equals("")) {
						newTexts.put(ctx, result+ s1 + " " + s2 + " " + s3); // ��� ���� ���� newTexts�� ����
					}
					else
						newTexts.put(ctx, s1 + " " + s2 + " " + s3);
						
				} else {
					// '--' expr || '++' expr
					s1 = ctx.getChild(0).getText();
					s2 = ctx.getChild(1).getText();
					s3 = newTexts.get(ctx.getChild(2));
					newTexts.put(ctx, s1 + s2 + s3); // ��� ���� ���� newTexts�� ����
				}
			}
		} else if (ctx.getChildCount() == 4) {
			// IDENT '[' expr ']' || IDENT '(' args ')'
			s1 = ctx.getChild(0).getText();
			s2 = ctx.getChild(1).getText();
			s3 = newTexts.get(ctx.getChild(2));
			s4 = ctx.getChild(3).getText();
			newTexts.put(ctx, s1 + s2 + s3 + s4); // ��� ���� ���� newTexts�� ����
		} else {
			// IDENT '[' expr ']' '=' expr
			s1 = ctx.getChild(0).getText();
			s2 = ctx.getChild(1).getText();
			s3 = newTexts.get(ctx.getChild(2));
			s4 = ctx.getChild(3).getText();
			s5 = ctx.getChild(4).getText();
			s6 = newTexts.get(ctx.getChild(5));
			newTexts.put(ctx, s1 + s2 + s3 + s4 + s5 + s6); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterArgs(MiniCParser.ArgsContext ctx) {
	}

	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		if (ctx.children == null) {
			// null �� ���
			newTexts.put(ctx, "");
		} else { // expr (',' expr)*
			String sum = ""; // ������ ������
			String[] s = new String[ctx.getChildCount()];
			s[0] = newTexts.get(ctx.getChild(0));
			for (int i = 1; i < ctx.getChildCount(); i += 2) {
				s[i] = ctx.getChild(i).getText();
				s[i + 1] = newTexts.get(ctx.getChild(i + 1));
			}
			for (int i = 0; i < ctx.getChildCount(); i++) {
				if (s[i].equals(",")) // ���Ұ� ,���
					sum += s[i] + " "; // ������ �߰�
				else // �� ���� ���Ҷ��
					sum += s[i]; // ������ �߰���������
			}
			newTexts.put(ctx, sum); // ��� ���� ���� newTexts�� ����
		}
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {

	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {

	}

	@Override
	public void visitTerminal(TerminalNode ctx) {
		newTexts.put(ctx, ctx.getText()); // Terminal�� newTexts�� ����
	}

	@Override
	public void visitErrorNode(ErrorNode ctx) {

	}

	private boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
		return ctx.getChildCount() == 3 && ctx.getChild(0) != ctx.IDENT() && ctx.getChild(1) != ctx.expr();
	}

	private String getNumOfDot(int a) { // nesting
		String s = "";
		for (int i = 0; i < a; i++)
			s += "	";
		return s;
	}

}
