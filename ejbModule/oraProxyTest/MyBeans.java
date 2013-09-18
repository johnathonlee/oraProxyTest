package oraProxyTest;

import javax.ejb.Remote;
import javax.ejb.Remove;

@Remote
public interface MyBeans {

	public abstract long getId();

	public abstract void setId(long id);

	public abstract String getMeaningless();

	public abstract void setMeaningless(String meaningless);

	public abstract void removeMe();

}