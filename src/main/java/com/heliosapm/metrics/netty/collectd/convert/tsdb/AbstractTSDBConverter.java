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
package com.heliosapm.metrics.netty.collectd.convert.tsdb;

import java.util.Collections;
import java.util.Map;

import com.heliosapm.metrics.netty.collectd.convert.ConverterType;
import com.heliosapm.metrics.netty.collectd.convert.IConverter;
import com.heliosapm.metrics.netty.collectd.type.DataSet;
import com.heliosapm.metrics.netty.collectd.type.DataSource;

/**
 * <p>Title: AbstractTSDBConverter</p>
 * <p>Description: Abstract base class for OpenTSDB converters</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.convert.tsdb.AbstractTSDBConverter</code></p>
 */

public abstract class AbstractTSDBConverter implements IConverter<OpenTSDBMetric> {

	@Override
	public ConverterType getConverterType() {		
		return ConverterType.TSDB;
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.metrics.netty.collectd.convert.IConverter#convert(long, long, com.heliosapm.metrics.netty.collectd.type.DataSet, com.heliosapm.metrics.netty.collectd.type.DataSource)
	 */
	@Override
	public OpenTSDBMetric convert(final long value, final long timestamp, final DataSet dataSet, final DataSource dataSource) {
		return new OpenTSDBMetric(timestamp, value, getMetricName(dataSet, dataSource), Collections.unmodifiableMap(getTags(dataSet, dataSource)));
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.metrics.netty.collectd.convert.IConverter#convert(double, long, com.heliosapm.metrics.netty.collectd.type.DataSet, com.heliosapm.metrics.netty.collectd.type.DataSource)
	 */
	@Override
	public OpenTSDBMetric convert(final double value, final long timestamp, final DataSet dataSet, final DataSource dataSource) {	
		return new OpenTSDBMetric(timestamp, value, getMetricName(dataSet, dataSource), Collections.unmodifiableMap(getTags(dataSet, dataSource)));
	}

	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.metrics.netty.collectd.convert.IConverter#convert(int, long, com.heliosapm.metrics.netty.collectd.type.DataSet, com.heliosapm.metrics.netty.collectd.type.DataSource)
	 */
	@Override
	public OpenTSDBMetric convert(final int value, final long timestamp, final DataSet dataSet, final DataSource dataSource) {
		return new OpenTSDBMetric(timestamp, value, getMetricName(dataSet, dataSource), Collections.unmodifiableMap(getTags(dataSet, dataSource)));
	}

	/**
	 * Returns the metric name for the passed data set and data source
	 * @param dataSet The data set
	 * @param dataSource The data source
	 * @return The converted metric name
	 */
	protected abstract String getMetricName(final DataSet dataSet, final DataSource dataSource);
	/**
	 * Returns the metric tags for the passed data set and data source
	 * @param dataSet The data set
	 * @param dataSource The data source
	 * @return The converted metric tags
	 */
	protected abstract Map<String, String> getTags(final DataSet dataSet, final DataSource dataSource);
	
	/**
	 * {@inheritDoc}
	 * @see com.heliosapm.metrics.netty.collectd.convert.IConverter#getConversionPattern()
	 */
	public abstract String getConversionPattern();
}
