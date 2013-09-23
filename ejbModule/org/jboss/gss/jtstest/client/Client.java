/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the VmTwoBeanOne Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the VmTwoBeanOne
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.gss.jtstest.client;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import oraProxyTest.MyBeans;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class Client implements ClientInVM {

	Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	public Client() {

	}

	public void beginWork() {
		log.info("beginWork");

		Properties prop = new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.URL_PKG_PREFIXES,
				"org.jboss.naming:org.jnp.interfaces");
		prop.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");

		InitialContext ctx;
		try {
			ctx = new InitialContext(prop);
		} catch (NamingException e1) {
			log.warning("fail on InitialContext");
			e1.printStackTrace();
			return;
		}

		log.info("InitialContext created for Client");
		log.info("------------------> startUtx()");

		log.info("------------------> getUtx()");
		UserTransaction utx = null;

		try {
			utx = (UserTransaction) ctx.lookup("UserTransaction");
		} catch (NamingException e1) {
			log.warning("fail finding UserTransaction");
			e1.printStackTrace();
			return;
		}
		log.info("------------------> UTX is initialized!");

		if (utx == null) {
			log.warning("Could not get UserTransaction");
			return;
		}

		try {
			utx.begin();
		} catch (NotSupportedException e1) {
			e1.printStackTrace();
			return;
		} catch (SystemException e1) {
			e1.printStackTrace();
			return;
		}

		MyBeans beanOne;
		try {
			beanOne = (MyBeans) PortableRemoteObject.narrow(
					ctx.lookup("Beanone/local"), MyBeans.class);
		} catch (ClassCastException e1) {
			e1.printStackTrace();
			return;
		} catch (NamingException e1) {
			e1.printStackTrace();
			return;
		}

		beanOne.setMeaningless("m1");

		log.info("persisting beanOne");
		beanOne.persist();
		
		try {
			   utx.rollback();
			} catch (SecurityException e) {
				try {
					utx.rollback();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (IllegalStateException e) {
				try {
					utx.rollback();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (SystemException e) {
				try {
					utx.rollback();
				} catch (IllegalStateException e1) {
					
					e1.printStackTrace();
				} catch (SecurityException e1) {
					
					e1.printStackTrace();
				} catch (SystemException e1) {
					
					e1.printStackTrace();
				}
				e.printStackTrace();
			} 		
	}

}
