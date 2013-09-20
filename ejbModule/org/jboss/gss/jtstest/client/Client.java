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
import javax.rmi.PortableRemoteObject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import oraProxyTest.MyBeans;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class Client implements ClientInVM {

	Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	public Client() {

	}

	public void beginWork() throws Exception {
		log.info("beginWork");

		Properties prop = new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.URL_PKG_PREFIXES,
				"org.jboss.naming:org.jnp.interfaces");
		prop.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");

		InitialContext ctx = new InitialContext(prop);

		log.info("InitialContext created for Client");
		log.info("------------------> startUtx()");

		log.info("------------------> getUtx()");
		UserTransaction utx = null;

		utx = (UserTransaction) ctx.lookup("UserTransaction");
		log.info("------------------> UTX is initialized!");

		if (utx == null) {
			log.warning("Could not get UserTransaction");
			return;
		}

		utx.begin();

		MyBeans beanOne = (MyBeans) PortableRemoteObject.narrow(
				ctx.lookup("Beanone/local"), MyBeans.class);

		beanOne.setMeaningless("m1");

		log.info("persisting beanOne");
		beanOne.persist();

		MyBeans beanTwo = (MyBeans) PortableRemoteObject.narrow(
				ctx.lookup("Beantwo/local"), MyBeans.class);

		//beanTwo.setMeaningless("m2");

		log.info("persisting beanTwo");
		beanTwo.persist();

		log.info(Integer.toString(utx.getStatus()));
		if (utx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
			utx.commit();
		} else {
			log.warning("Status.STATUS_MARKED_ROLLBACK");
		}

	}

}
