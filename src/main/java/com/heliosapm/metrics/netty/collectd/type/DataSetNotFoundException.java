/**
 * Helios, OpenSource Monitoring
 * Brought to you by the Helios Development Group
 *
 * Copyright 2015, Helios Development Group and individual contributors
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
 * <p>Title: DataSetNotFoundException</p>
 * <p>Description: Exception thrown when a DataSet lookup fails</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.type.DataSetNotFoundException</code></p>
 */

public class DataSetNotFoundException extends TypeDBException {

	/**  */
	private static final long serialVersionUID = 354428608343670251L;

	/**
	 * Creates a new DataSetNotFoundException
	 */
	public DataSetNotFoundException() {
	}

	/**
	 * Creates a new DataSetNotFoundException
	 * @param message The error message
	 */
	public DataSetNotFoundException(String message) {
		super(message);
	}

	/**
	 * Creates a new DataSetNotFoundException
	 * @param cause The underlying cause
	 */
	public DataSetNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new DataSetNotFoundException
	 * @param message The error message
	 * @param cause The underlying cause
	 */
	public DataSetNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
