package oraProxyTest;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.log4j.Logger;


/**
 * The persistent class for the BEANONE database table.
 * 
 */
@Entity
@Stateful
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@NamedQuery(name="Beanone.findAll", query="SELECT b FROM Beanone b")
public class Beanone implements Serializable, MyBeans {
	
	@PersistenceContext
	@Transient
	private EntityManager manager;
	
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BEANONE_ID_GENERATOR", sequenceName="HIBSEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BEANONE_ID_GENERATOR")
	private long id;

	private String meaningless;

	@Transient
	private Logger log = Logger.getLogger(Beanone.class);

	public Beanone() {
	}

	/* (non-Javadoc)
	 * @see oraProxyTest.MyBeans#getId()
	 */
	public long getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see oraProxyTest.MyBeans#setId(long)
	 */
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see oraProxyTest.MyBeans#getMeaningless()
	 */
	public String getMeaningless() {
		return this.meaningless;
	}

	/* (non-Javadoc)
	 * @see oraProxyTest.MyBeans#setMeaningless(java.lang.String)
	 */
	public void setMeaningless(String meaningless) {
		this.meaningless = meaningless;
	}

	/* (non-Javadoc)
	 * @see oraProxyTest.MyBeans#removeMe()
	 */
	@Remove
	public void removeMe() {
		log.info("tried to remove");
		manager.persist(this);
	}

	public void persist() {
		log.info("persist");
		manager.persist(this);
		manager.flush();
	}

}