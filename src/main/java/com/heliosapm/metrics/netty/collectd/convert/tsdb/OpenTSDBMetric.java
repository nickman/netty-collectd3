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

import java.util.Map;

/**
 * <p>Title: OpenTSDBMetric</p>
 * <p>Description: Represents an OpenTSDB converted metric</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.convert.tsdb.OpenTSDBMetric</code></p>
 */

public class OpenTSDBMetric {
	/** The metric timestamp */
	public final long timestamp;
	/** The metric long value */
	public final long longValue;
	/** The metric double value */
	public final double doubleValue;
	/** Indicates if the metric is a double value (true) or a long value (false) */
	public final boolean dbl;
	/** The OpenTSDB metric name */
	public final String metricName;
	/** The OpenTSDB tags */
	public final Map<String, String> tags;
	
	/**
	 * Creates a new long value OpenTSDBMetric
	 * @param timestamp The metric timestamp
	 * @param longValue The metric long value
	 * @param metricName The OpenTSDB metric name
	 * @param tags The OpenTSDB tags
	 */
	public OpenTSDBMetric(final long timestamp, final long longValue, final String metricName, final Map<String, String> tags) {
		this.timestamp = timestamp;
		this.longValue = longValue;
		this.doubleValue = 0D;
		this.dbl = false;
		this.metricName = metricName;
		this.tags = tags;
	}
	
	/**
	 * Creates a new double value OpenTSDBMetric
	 * @param timestamp The metric timestamp
	 * @param doubleValue The metric long value
	 * @param metricName The OpenTSDB metric name
	 * @param tags The OpenTSDB tags
	 */
	public OpenTSDBMetric(final long timestamp, final double doubleValue, final String metricName, final Map<String, String> tags) {
		this.timestamp = timestamp;
		this.longValue = 0;
		this.doubleValue = doubleValue;
		this.dbl = false;
		this.metricName = metricName;
		this.tags = tags;
	}
	
	
}
