//frame for function call, args, temp vars
package Interpreter;

import java.util.*;
import Parser.ASTs.*;

public class RT_Frame {
	RT_Env crt_env;
	RT_Frame return_frm;
	Data_Obj return_obj;
	Data_Func crt_func;
	ArrayList<Data_Obj> args=new ArrayList<Data_Obj>();
	//HashMap<String,Data_Obj> tmp_vars;
	public RT_Env getCrtEnv(){
		return this.crt_env;
	}
	public boolean setCrtEnv(RT_Env crt_env){
		this.crt_env=crt_env;
		return true;
	}
	public RT_Frame getRtnFrm(){
		return this.return_frm;
	}
	public boolean setRtnFrm(RT_Frame return_frm){
		this.return_frm=return_frm;
		return true;
	}
	public Data_Obj getRtnObj(){
		return this.return_obj;
	}
	public boolean setRtnObj(Data_Obj return_obj){
		this.return_obj=return_obj;
		return true;
	}
	public ArrayList<Data_Obj> getArgs(){
		return this.args;
	}
	
}