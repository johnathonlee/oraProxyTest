package oraProxyTest;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
public interface MyBeans {

	public abstract long getId();

	public abstract void setId(long id);

	public abstract String getMeaningless();

	public abstract void setMeaningless(String meaningless);

	public abstract void removeMe();
	
	public abstract void persist();

}