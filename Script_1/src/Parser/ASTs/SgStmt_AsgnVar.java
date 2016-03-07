package Parser.ASTs;

import Parser.*;
import Parser.IR.*;

public class SgStmt_AsgnVar extends AST {
	Expr_Left left_hand;
	Expr expr;
	
	public Expr_Left getLeft() {
		return left_hand;
	}
	public void setLeft(Expr_Left left_hand) {
		this.left_hand = left_hand;
	}
	public Expr getExpr() {
		return expr;
	}
	public void setExpr(Expr expr) {
		this.expr = expr;
	}
	public boolean genCode(CodeGenerator codegen){
		this.left_hand.genCode(codegen);
		this.expr.genCode(codegen);
		IRCode code;
		code=new IRCode("cpy",this.left_hand.ref_type,this.left_hand.tmp_addr,this.expr.getRst());		
		codegen.addCode(code);
		codegen.incLineNo();
		return true;
	}
	public boolean checkType(CodeGenerator codegen){
		if(!this.left_hand.checkType(codegen))
			return false;
		this.expr.asgn_type=this.left_hand.ref_type;
		return this.expr.checkType();
	}
}