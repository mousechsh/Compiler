package Parser.TypeSys;

import java.util.*;

public class T_Interface extends T_Type {
	LinkedList<T_Interface> extd_types=new LinkedList<T_Interface>();
	LinkedList<T_Function> methods=new LinkedList<T_Function>();
	HashMap<String,R_Function> name_methods=new HashMap<String,R_Function>();
	
	public boolean isSuperOf(T_Type type1){
		return true;
	}
	public boolean isSubOf(T_Type type1){
		return true;
	}
	public LinkedList<T_Function> getMethods() {
		return methods;
	}
	public void setMethods(LinkedList<T_Function> methods) {
		this.methods = methods;
		for(T_Function f:this.methods){
			if(!this.name_methods.containsKey(f.type_name)){
				this.name_methods.put(f.type_name, new R_Function());
			}
			this.name_methods.get(f.type_name).setTypeT(f);			
		}
	}
	public LinkedList<T_Interface> getExtdTypes() {
		return extd_types;
	}
	public void setExtdTypes(LinkedList<T_Interface> extd_types) {
		this.extd_types = extd_types;
		/*for(T_Interface i:this.extd_types){
			for(T_Function f:i.methods){
				this.methods.add(f);
				if(!this.name_methods.containsKey(f.type_name)){
					this.name_methods.put(f.type_name, new R_Function());
				}
				this.name_methods.get(f.type_name).setTypeT(f);		
			}
		}*/
	}
	public HashMap<String, R_Function> getNameMethods() {
		return name_methods;
	}
	public void setNameMethods(HashMap<String, R_Function> name_methods) {
		this.name_methods = name_methods;
	}
}
