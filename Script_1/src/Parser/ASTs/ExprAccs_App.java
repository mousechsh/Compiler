package Parser.ASTs;

import Parser.*;
import Parser.IR.*;
import Parser.TypeSys.*;
import java.util.*;

public class ExprAccs_App extends AST {
	ExprAccs pre_accs;
	Gnrc_ArgLst gnrc_args;
	ExprPri_Var var;
	FuncApp_ArgLst arg_lst;
	String rst_val;
	String ref_type;
	String rst_type;
	//String ptr_func;//pointer to function
	String ptr_scp;//search scope for function
	String func_name;
	String func_sig;
	boolean inGType=false;
	
	public void lnkApp(ExprAccs pre_accs,Gnrc_ArgLst g_lst,ExprPri_Var var,FuncApp_ArgLst arg_lst){
		this.pre_accs=pre_accs;
		this.gnrc_args=g_lst;
		this.var=var;
		this.arg_lst=arg_lst;
	}
	public void setApp(Gnrc_ArgLst g_lst, ExprPri_Var var, FuncApp_ArgLst arg_lst){
		this.var=var;
		this.arg_lst=arg_lst;
		this.gnrc_args=g_lst;
	}
	public boolean genCode(CodeGenerator codegen)throws GenCodeException{
		if(this.pre_accs!=null)
			this.pre_accs.genCode(codegen);
		//if(this.gnrc_args!=null)
		//	this.gnrc_args.genCode(codegen);
		//if(this.arg_lst!=null)
		//	this.arg_lst.genCode(codegen);
		IRCode code=null;
		if(this.ptr_scp!=null){
			code=new IRCode("getFunc",this.func_name, this.func_sig,this.ptr_scp);
			codegen.addCode(code);
		}
		if(this.gnrc_args!=null){
			this.gnrc_args.genCode(codegen);
		}
		if(this.arg_lst!=null){
			this.arg_lst.genCode(codegen);
		}
		if(this.ptr_scp.equals("this")){
			code=new IRCode("pushThis",null,null,null);
			codegen.addCode(code);
		}//TODO
		code=new IRCode("invoke",this.rst_val,this.rst_type,null);
		codegen.addCode(code);
		return true;
	}
	public boolean genSymTb(CodeGenerator codegen)throws GenSymTblException{
		if(this.pre_accs!=null){
			if(!this.pre_accs.genSymTb(codegen))return false;
			if(this.pre_accs.rst_val.equals("this"))
				this.ptr_scp="this";
		}
		if(this.gnrc_args!=null&&!this.gnrc_args.genSymTb(codegen))
			return false;
		if(this.arg_lst!=null&&!this.arg_lst.genSymTb(codegen))
			return false;
		this.rst_val="%"+codegen.getTmpSn();
		this.func_name=this.var.rst_val;
		return true;
	}
	public boolean checkType(CodeGenerator codegen)throws TypeCheckException{
		if(this.pre_accs!=null&&!this.pre_accs.checkType(codegen))
			return false;
		if(this.gnrc_args!=null&&!this.gnrc_args.checkType(codegen))
			return false;
		if(this.arg_lst!=null&&!this.arg_lst.checkType(codegen))
			return false;
		R_Function f=null;
		R_Variable r=new R_Variable();
		//this.ptr_func="*"+codegen.getTmpSn();
		//this.rst_type
		//R_Variable		
		//this.ptr_func
		//this.ptr_scp
		//this.func_sig
		if(this.pre_accs==null||this.pre_accs.rst_val.equals("this")){// f()/this.f()
			f=codegen.getFuncInSymTb(this.var.name);
			if(f==null)
				throw new TypeCheckException("Type Check Error: not defined function "+this.var.name);
			if(this.ptr_scp==null){
				if(codegen.isScopeIn("global"))
					this.ptr_scp="global";
				else
					throw new TypeCheckException("Type Check Error: scope "+this.var.name);
			}else if(this.ptr_scp.equals("this")){
				if(!codegen.isScopeIn("class"))
					throw new TypeCheckException("Type Check Error: not in class scope"+this.var.name);
			}
		}else{//if this.pre_accs!=null&&!this.pre_accs.rst_val.equals("this")
			if(this.pre_accs.rst_type.equals("class")){//A.class.f()
				T_Generic t=new T_Generic();
				t.setCoreType("class"); //t is class<A>
				LinkedList<String> l=new LinkedList<String>();
				l.add(this.pre_accs.rst_val);
				t.setGnrcPars(l);
				this.ptr_scp="<"+codegen.getTmpSn();
				codegen.putTypeInSymTb(this.ptr_scp, t);
				R_Variable r1=new R_Variable();
				r1.setVarName(this.ptr_scp);
				r1.setVarType("class");
				r1.setTmpAddr(this.ptr_scp);
				codegen.putVarInSymTb(this.ptr_scp, r1);
				T_Class t1=((T_Class)codegen.getTypeInSymTb("class"));				
				f=t1.getMethods().get(this.func_name);
				codegen.gnrc_arg.addFirst(new HashMap<String,String>());
				codegen.gnrc_arg.getFirst().put("T", this.ptr_scp);
				this.inGType=true;
			}else if(codegen.getVarInSymTb(this.pre_accs.rst_val)!=null
					||codegen.getTypeInSymTb(this.pre_accs.rst_val)!=null){// a.f()/A.f()
				this.ptr_scp=this.pre_accs.rst_val;
				T_Type t=codegen.getTypeInSymTb(codegen.getVarInSymTb(this.pre_accs.rst_val).getVarType());
				if(t.getKType()==T_Type.KType.t_cls){
					f=((T_Class)t).getMethods().get(this.func_name);
				}else if(t.getKType()==T_Type.KType.t_gnrc){
					T_Type t1=codegen.getTypeInSymTb(((T_Generic)t).getCoreType());
					f=((T_Class)t1).getMethods().get(this.var.name);
					codegen.gnrc_arg.addFirst(((T_Generic)t).getTypeArgTb());
					this.inGType=true;
				}else

					throw new TypeCheckException("Type Check Error:  "+this.var.name);
			}else

				throw new TypeCheckException("Type Check Error:  "+this.var.name);
		}
		LinkedList<String> args1=(this.gnrc_args==null?null:this.gnrc_args.getTypesName());
		LinkedList<String> args2=(this.arg_lst==null?null:this.arg_lst.arg_types);
		if(!f.isMulti()){
			if(f.isEqArgTypes(codegen, args1, args2)
					||f.isCtArgTypes(codegen, args1, args2)){
				this.rst_type=((T_Function)(f.getTypeT())).getRetType();
				this.func_sig=f.getFuncSig();
			}
			else
				throw new TypeCheckException("Type Check Error:  "+this.var.name);
		}else{
			for(R_Function f1:f.getMulti().values()){
				if(f.isEqArgTypes(codegen, args1, args2)
						||f.isCtArgTypes(codegen, args1, args2)){
					this.rst_type=((T_Function)(f.getTypeT())).getRetType();
					this.func_sig=f1.getFuncSig();
				}						
			}
			if(this.rst_type==null)
				throw new TypeCheckException("Type Check Error:  "+this.var.name);
		}
		r=new R_Variable();
		r.setVarName(this.rst_val);
		r.setVarType(this.rst_type);
		r.setTmpAddr(this.rst_val);
		codegen.putVarInSymTb(this.rst_val, r);
		if(this.ref_type!=null&&!codegen.getTypeInSymTb(this.ref_type).canAsnFrom(codegen, codegen.getTypeInSymTb(this.rst_type)))
			throw new TypeCheckException("Type Check Error:  "+this.var.name);
		if(this.inGType)
			codegen.gnrc_arg.remove();
		return true;		
	}
}
