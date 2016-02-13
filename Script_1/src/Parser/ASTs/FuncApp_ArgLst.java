package Parser.ASTs;

import java.util.*;

import Parser.*;

public class FuncApp_ArgLst extends AST {
	LinkedList<Expr_Calc> args;
	boolean isE=false;
	public boolean isE() {
		return isE;
	}
	public void setE() {
		this.isE = true;
	}
	public void addArg(Expr_Calc ast){
		if(this.args==null){
			this.args=new LinkedList<Expr_Calc>();
		}
		this.args.add(ast);
	}
}