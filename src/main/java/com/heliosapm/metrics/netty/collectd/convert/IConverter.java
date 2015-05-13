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
package com.heliosapm.metrics.netty.collectd.convert;

import com.heliosapm.metrics.netty.collectd.type.DataSet;
import com.heliosapm.metrics.netty.collectd.type.DataSource;

/**
 * <p>Title: IConverter</p>
 * <p>Description: Defines a conveter which converts a metric submission to a specific metric format</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.convert.IConverter</code></p>
 * @param <T> The expected type of the converted metric
 */

public interface IConverter<T> {
	/**
	 * Returns this converter's type
	 * @return this converter's type
	 */
	public ConverterType getConverterType();
	
	/**
	 * Returns the original conversion pattern
	 * @return the original conversion pattern
	 */
	public String getConversionPattern();
	
	/**
	 * Converts the passed metric submission to an endpoint specific type instance
	 * @param value The metric value
	 * @param timestamp The metric timestamp
	 * @param dataSet The associated data set
	 * @param dataSource The associated data source
	 * @return the converted instance
	 */
	public T convert(long value, long timestamp, DataSet dataSet, DataSource dataSource);
	
	/**
	 * Converts the passed metric submission to an endpoint specific type instance
	 * @param value The metric value
	 * @param timestamp The metric timestamp
	 * @param dataSet The associated data set
	 * @param dataSource The associated data source
	 * @return the converted instance
	 */
	public T convert(double value, long timestamp, DataSet dataSet, DataSource dataSource);
	
	/**
	 * Converts the passed metric submission to an endpoint specific type instance
	 * @param value The metric value
	 * @param timestamp The metric timestamp
	 * @param dataSet The associated data set
	 * @param dataSource The associated data source
	 * @return the converted instance
	 */
	public T convert(int value, long timestamp, DataSet dataSet, DataSource dataSource);

	
}
