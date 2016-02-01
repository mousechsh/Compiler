package Parser.ASTs;

import java.util.*;
import Parser.*;

public class MbrDef_Lst extends AST {
	boolean isE=false;
	LinkedList<MbrDef> mbrs;
	public void addMbr(MbrDef par){
		if(this.mbrs==null){
			this.mbrs=new LinkedList<MbrDef>();			
		}
		this.mbrs.add(par);
		this.upAll(par);
	}
	public boolean isE() {
		return isE;
	}
	public void setE() {
		this.isE = true;
	}
}
