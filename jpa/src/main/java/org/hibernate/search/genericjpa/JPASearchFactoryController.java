/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.genericjpa;

import javax.persistence.EntityManager;
import java.io.Closeable;

import org.hibernate.search.SearchFactory;
import org.hibernate.search.genericjpa.db.events.UpdateConsumer;
import org.hibernate.search.jpa.FullTextEntityManager;

/**
 * This interface is the main entry point to get Search working in your JPA application
 *
 * @author Martin Braun
 */
public interface JPASearchFactoryController extends Closeable {

	SearchFactory getSearchFactory();

	void pauseUpdating(boolean pause);

	FullTextEntityManager getFullTextEntityManager(EntityManager em);

	void addUpdateConsumer(UpdateConsumer updateConsumer);

	void removeUpdateConsumer(UpdateConsumer updateConsumer);

}
