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
package com.heliosapm.metrics.netty.collectd.type;

/**
 * <p>Title: DataType</p>
 * <p>Description: Enumerates the collectd data types</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.type.DataType</code></p>
 */

public enum DataType {
	/**  */
	ABSOLUTE,
	/**  */
	COUNTER,
	/**  */
	DERIVE,
	/**  */
	GAUGE;
	
	/**
	 * Decodes the passed stringy to a DataType
	 * @param stringy The name to decode which is trimmed and upper cased.
	 * @return The decoded DataType
	 */
	public static DataType decode(final CharSequence stringy) {
		if(stringy==null) throw new IllegalArgumentException("The passed value to decode was null");
		final String s = stringy.toString().trim().toUpperCase();
		if(s.isEmpty()) throw new IllegalArgumentException("The passed value to decode was empty");
		try {
			return valueOf(s);
		} catch (Exception ex) {
			throw new IllegalArgumentException("The passed value [" + stringy + "] is not a valid DataType");
		}
	}
}
