package Parser.TypeSys;

import java.util.*;
import Parser.*;

public class T_Class extends T_Type {
	private LinkedList<T_Interface> impl_types=new LinkedList<T_Interface>();
	private LinkedList<T_Class> extd_types=new LinkedList<T_Class>();
	private HashMap<String,R_Function> methods=new HashMap<String,R_Function>();
	private HashMap<String,R_Variable> fields=new HashMap<String,R_Variable>();
	private HashSet<String> all_impl=new HashSet<String>();	
	private HashSet<String> all_extd=new HashSet<String>();
	
	
	public HashMap<String,R_Variable> getFields() {
		return fields;
	}
	public void setFields(HashMap<String,R_Variable> fields) {
		this.fields = fields;
	}
	public HashMap<String,R_Function> getMethods() {
		return methods;
	}
	public void setMethods(HashMap<String,R_Function> methods) {
		this.methods = methods;
	}
	public LinkedList<T_Interface> getImplTypes() {
		return impl_types;
	}
	public void setImplTypes(LinkedList<T_Interface> impl_types) {
		this.impl_types = impl_types;
	}
	public LinkedList<T_Class> getExtdTypes() {
		return extd_types;
	}
	public void setExtdTypes(LinkedList<T_Class> extd_types) {
		this.extd_types = extd_types;
	}
	public HashSet<String> getAllImpl() {
		return all_impl;
	}
	public HashSet<String> getAllExtd() {
		return all_extd;
	}
	public void genTypeCode() {
		String s=this.getTypeName();
		if(this.isGnrc())
			s=s+"<"+this.getGnrcPars().size()+">";
		this.setTypeCode(s);
	}
	//for type checking
	public boolean checkAllImpl(CodeGenerator codegen){//no ring inheriting in impl types 
		for(T_Interface t:this.impl_types){
			if(this.all_impl.contains(t.getTypeName()))return false;
			this.all_impl.add(t.getTypeName());
			for(String name:t.getAllExtd()){
				if(this.all_impl.contains(name))return false;
				this.all_impl.add(name);
			}		
		}
		for(T_Class t:this.extd_types){
			for(String name:t.getAllImpl()){
				if(this.all_impl.contains(name))return false;
				this.all_impl.add(name);
			}
		}
		return true;
	}
	public boolean checkAllExtd(CodeGenerator codegen){//no ring inheriting in extd types
		for(T_Class t:this.extd_types){
			for(String name:t.getAllExtd()){
				if(this.all_extd.contains(name))return false;
				this.all_extd.add(name);
			}
		}
		return true;
	}
	public boolean checkAllMthd(CodeGenerator codegen){
		for(T_Interface t:this.impl_types){//make sure all interface are implemented
			for(String name:t.getMethods().keySet()){
				if(!this.methods.containsKey(name)) return false;
				if(!this.methods.get(name).isEqNameType(t.getMethods().get(name))){
					return false;
				}				
			}
		}
		for(T_Class t:this.extd_types){//gen all methods in this type, including super classes's
			for(String name:t.methods.keySet()){
				R_Function r=t.methods.get(name);
				if(!this.methods.containsKey(name)){
					this.methods.put(name, r);
				}else{
					R_Function r1=this.methods.get(name);
					if(r.isMulti()){
						for(String ts:r.getMulti().keySet()){
							if(!r1.isCntnNameType(r.getMulti().get(ts))){
								r1.addFuncR(r.getMulti().get(ts));
							}
						}
					}else{
						if(!r1.isCntnNameType(r))
							r1.addFuncR(r);
					}
				}
			}
		}
		return true;
	}
	public boolean checkAllField(CodeGenerator codegen){//gen all fields in this type, including super classes's
		for(T_Class t:this.extd_types){
			for(String name:t.fields.keySet()){
				if(!this.fields.containsKey(name)){
					this.fields.put(name, t.fields.get(name));
				}
			}
		}
		return true;
	}
}