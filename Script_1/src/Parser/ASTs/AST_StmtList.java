package Parser.ASTs;

import java.util.*;

import Interpreter.*;
import Parser.*;

public class AST_StmtList extends AST {
	private ArrayList<AST> stmt_list=new ArrayList<AST>();
	
	public void addStmt(AST stmt){
		this.stmt_list.add(stmt);
	}
	public boolean genCode(CodeGenerator codegen){
		codegen.pushBlock4Sym(this);
		for(AST stmt:this.stmt_list){
			stmt.genCode(codegen);
		}
		codegen.popBlock4Sym();
		return true;
	}
	public boolean genSymTb(CodeGenerator codegen){
		codegen.pushBlock4Sym(this);
		for(int i=0;i<this.stmt_list.size();i++){
			AST stmt=this.stmt_list.get(i);
			stmt.genSymTb(codegen);
		}
		codegen.popBlock4Sym();
		return true;
	}
	public boolean checkType(CodeGenerator codegen){
		boolean rst=true;
		codegen.pushBlock4Sym(this);
		for(int i=0;i<this.stmt_list.size();i++){
			AST stmt=this.stmt_list.get(i);			
			if(!stmt.checkType(codegen)){
				//TODO try recover
				rst=false;
				continue;
			}
		}
		codegen.popBlock4Sym();
		return rst;
	}
}
