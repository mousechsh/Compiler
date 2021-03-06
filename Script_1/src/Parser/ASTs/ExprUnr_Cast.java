package Parser.ASTs;

import Parser.*;
import Parser.IR.*;
import Parser.TypeSys.*;

public class ExprUnr_Cast extends AST {
	TypeExp type_exp;
	ExprAccs accs;
	String rst_val;
	String rst_type;
	String ref_type;
	
	public boolean setCast(TypeExp type_exp,ExprAccs accs){
		this.type_exp=type_exp;
		this.accs=accs;
		return true;
	}
	public boolean genCode(CodeGenerator codegen)throws GenCodeException{
		
		IRCode code=new IRCode("cast",this.accs.rst_type+"->"+this.ref_type,this.rst_val,this.accs.rst_val);
		codegen.addCode(code);
		return true;
	}
	public boolean genSymTb(CodeGenerator codegen)throws GenSymTblException{
		if(!this.type_exp.genSymTb(codegen))
			return false;
		if(!this.accs.genSymTb(codegen))
			return false;
		this.rst_val="%"+codegen.getTmpSn();
		R_Variable r=new R_Variable();
		r.setVarName(this.rst_val);
		r.addRstVal(this.rst_val);
		this.rst_type=this.ref_type;
		r.setVarType(this.ref_type);
		codegen.putVarInSymTb(this.rst_val, r);
		return true;
	}
	public boolean checkType(CodeGenerator codegen)throws TypeCheckException{
		if(!this.type_exp.checkType(codegen))
			return false;
		if(!this.accs.checkType(codegen))
			return false;
		if(!codegen.getTypeInSymTb(this.ref_type).canCastFrom(codegen, codegen.getTypeInSymTb(this.rst_type)))
			throw new TypeCheckException("type check error: cast "+this.rst_type);
		return true;
	}
}
