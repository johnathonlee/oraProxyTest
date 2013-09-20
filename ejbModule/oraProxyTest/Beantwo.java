package oraProxyTest;

import java.io.Serializable;

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
 * The persistent class for the BEANTWO database table.
 * 
 */
@Entity
@Stateful
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@NamedQuery(name="Beantwo.findAll", query="SELECT b FROM Beantwo b")
public class Beantwo implements Serializable, MyBeans{
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	@Transient
	private EntityManager manager;

	@Id
	@SequenceGenerator(name="BEANTWO_ID_GENERATOR", sequenceName="HIBSEQUENCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BEANTWO_ID_GENERATOR")
	private long id;
	
	@Transient
	private Logger log = Logger.getLogger(Beanone.class);

	private String meaningless;

	public Beantwo() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMeaningless() {
		return this.meaningless;
	}

	public void setMeaningless(String meaningless) {
		this.meaningless = meaningless;
	}

	public void removeMe() {
		manager.persist(this);
	}
	
	public void persist() {
		log.info("persist");
		manager.persist(this);
		manager.flush();
	}
}