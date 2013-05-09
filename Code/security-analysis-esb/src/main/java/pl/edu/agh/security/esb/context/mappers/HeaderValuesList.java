package pl.edu.agh.security.esb.context.mappers;

import java.util.ArrayList;

public class HeaderValuesList<E> extends ArrayList<E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		if(this.size() != 1) {
			return super.toString();
		}
		return this.get(0).toString();
	}
}
