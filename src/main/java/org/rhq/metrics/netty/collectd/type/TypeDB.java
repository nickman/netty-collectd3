/**
 * Helios, OpenSource Monitoring
 * Brought to you by the Helios Development Group
 *
 * Copyright 2007, Helios Development Group and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 *
 */
package org.rhq.metrics.netty.collectd.type;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: TypeDB</p>
 * <p>Description: Represents the collectd <b>types.db</b> file. 
 * See <a href="http://collectd.org/documentation/manpages/types.db.5.shtml">Manpage types.db(5)</a></p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.rhq.metrics.netty.collectd.type.TypeDB</code></p>
 */

public class TypeDB {
	/** The singleton instance */
	private static volatile TypeDB instance = null;
	/** The singleton instance ctor lock */
	private static final Object lock = new Object();
	
	/** Instance logger */
	private final Logger log = LoggerFactory.getLogger(getClass());
	/** A set of types.db file names */
	private final Set<String> fileNames = new LinkedHashSet<String>();

	
	/**
	 * Acquires the TypeDB singleton instance
	 * @return the TypeDB singleton instance
	 */
	public static TypeDB getInstance() {
		if(instance==null) {
			synchronized(lock) {
				if(instance==null) {
					instance = new TypeDB();
				}
			}
		}
		return instance;
	}
	
	private TypeDB() {
		
		log.info("Created TypeDB");
	}
	
	/**
	 * TypeDB load test
	 * @param args None
	 */
	public static void main(String[] args) {

	}

}
