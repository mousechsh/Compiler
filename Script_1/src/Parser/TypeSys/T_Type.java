package Parser.TypeSys;

import java.util.*;

public class T_Type {
	private KType k_type;//kind of type
	private String type_name;
	private String type_code;//code types for hash, search, compare
	private boolean isGnrc;//has generic pars as a core type
	private LinkedList<String> pars_Gnrc=new LinkedList<String>();	
	
	
	public boolean isEqType(T_Type t){
		if(this.type_code.equals(t.type_code))
			return true;
		return false;
	}
	
	//sets and gets	
	public boolean isGnrc() {
		return isGnrc;
	}
	public void setGnrc(boolean hasGnrcPar) {
		this.isGnrc = hasGnrcPar;
	}
	public String getTypeCode() {
		return type_code;
	}
	public void genTypeCode(){
		this.type_code=this.type_name;
	}
	public void setTypeCode(String type_code) {
		this.type_code = type_code;
	}
	public LinkedList<String> getGnrcPars() {
		return pars_Gnrc;
	}
	public void setGnrcPars(LinkedList<String> pars_Gnrc) {
		this.pars_Gnrc = pars_Gnrc;
	}
	public String getTypeName() {
		return type_name;
	}
	public void setTypeName(String type_name) {
		this.type_name = type_name;
	}
	public KType getKType() {
		return k_type;
	}
	public void setKType(KType k_type) {
		this.k_type = k_type;
	}
	public enum KType{t_bsc,t_arr,t_func,t_gnrc,t_cls,t_intf}
}
