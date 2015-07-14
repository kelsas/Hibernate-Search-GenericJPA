/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.genericjpa.db.events;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Braun
 */
public interface UpdateConsumer {

	/**
	 * called everytime an update is found in the database
	 *
	 * @param updateInfo a list of objects describing the several updates in the order they occured in the database
	 */
	void updateEvent(List<UpdateInfo> updateInfo);

	class UpdateInfo {

		private final Class<?> entityClass;
		private final Object id;
		private final int eventType;
		private final List<String> hints;

		public UpdateInfo(Class<?> entityClass, Object id, int eventType) {
			this( entityClass, id, eventType, Collections.emptyList() );
		}

		public UpdateInfo(Class<?> entityClass, Object id, int eventType, List<String> hints) {
			super();
			this.entityClass = entityClass;
			this.id = id;
			this.eventType = eventType;
			this.hints = hints;
		}

		/**
		 * @return the id
		 */
		public Object getId() {
			return this.id;
		}

		/**
		 * @return the eventType
		 */
		public int getEventType() {
			return this.eventType;
		}

		/**
		 * @return the entityClass
		 */
		public Class<?> getEntityClass() {
			return entityClass;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			UpdateInfo that = (UpdateInfo) o;

			if ( eventType != that.eventType ) {
				return false;
			}
			if ( entityClass != null ? !entityClass.equals( that.entityClass ) : that.entityClass != null ) {
				return false;
			}
			if ( id != null ? !id.equals( that.id ) : that.id != null ) {
				return false;
			}
			return !(hints != null ? !hints.equals( that.hints ) : that.hints != null);

		}

		@Override
		public int hashCode() {
			int result = entityClass != null ? entityClass.hashCode() : 0;
			result = 31 * result + (id != null ? id.hashCode() : 0);
			result = 31 * result + eventType;
			result = 31 * result + (hints != null ? hints.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "UpdateInfo{" +
					"entityClass=" + entityClass +
					", id=" + id +
					", eventType=" + eventType +
					", hints=" + hints +
					'}';
		}
	}

}
