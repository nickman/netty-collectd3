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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * <p>Title: DataSource</p>
 * <p>Description: Represents a data-source specification in a collectd <b>types.db</b> data set definition.
 * The structure is defined as <b><code>ds-name:ds-type:min:max</code></b>.</p>
 * <p>Examples:<ul>
 * 	<li><b><code>value:COUNTER:0:4294967295</code></b></li>
 *  <li><b><code>value:GAUGE:U:U</code></b></li>
 *  <li><b><code>seconds:GAUGE:-1000000:1000000</code></b></li>
 * </ul></p>
 * <p>See <a href="http://collectd.org/documentation/manpages/types.db.5.shtml">Manpage types.db(5)</a></p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.type.DataSource</code></p>
 */

public class DataSource {
	/** The data source name */
	private final String name;
	/** The data source type */
	private final DataType type;
	/** The data source minimum value */
	private final double minValue;
	/** The data source maximum value */
	private final double maxValue;
	
	/** A cache of DataSources keyed by the constructing entry text */
	private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>(512);
	
	/** A colon splitter expression */
	static final Pattern COLON_SPLITTER = Pattern.compile(":");
	
	/** The symbol for an unknown range value */
	static final String UNKNOWN = "U";
	
	/**
	 * Returns the DataSource for the passed string definition
	 * @param entry The string definition for the data source
	 * @return the DataSource
	 */
	public static DataSource getDataSource(final String entry) {
		if(entry==null || entry.trim().isEmpty()) throw new IllegalArgumentException("The passed DataSource definition was null or empty");
		final String key = entry.replace(" ", "");
		DataSource ds = dataSources.get(key);
		if(ds==null) {
			synchronized(dataSources) {
				ds = dataSources.get(key);
				if(ds==null) {
					ds = new DataSource(entry);
					dataSources.put(key, ds);
				}
			}
		}
		return ds;
	}
	
	private DataSource(final String entry) {
		if(entry==null || entry.trim().isEmpty()) throw new IllegalArgumentException("The passed DataSource definition was null or empty");
		final String[] frags = COLON_SPLITTER.split(entry.trim());
		if(frags.length!=4) throw new IllegalArgumentException("The passed DataSource definition was invalid [" + entry + "]. Did not have 4 segments.");
		for(int i = 0; i < 4; i++) {
			frags[i] = frags[i].trim();
			if(frags[i].isEmpty()) throw new IllegalArgumentException("The passed DataSource definition was invalid [" + entry + "]. Segments [" + i + "] was empty");
		}
		name = frags[0];
		type = DataType.decode(frags[1]);
		minValue = decode(frags[2]);
		maxValue = decode(frags[3]);
	}
	
	
	/**
	 * Parses a range value string to a double
	 * @param s The value to parse
	 * @return The parsed double or <b><code>NaN</code></b> if the string value was <b><code>U</code></b>.
	 */
	public static double decode(final String s) {
		if(s==null || s.trim().isEmpty()) throw new IllegalArgumentException("The passed numeric string was null or empty");
		final String _s = s.trim().toUpperCase();
		if(UNKNOWN.equals(_s)) return Double.NaN;
		try {
			return Double.parseDouble(_s);
		} catch (Exception ex) {
			throw new IllegalArgumentException("The passed numeric string [" + s + "] could not be parsed as a double");
		}
	}


	/**
	 * Returns the data source name
	 * @return the data source name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Returns the data source type
	 * @return the data source type
	 */
	public DataType getType() {
		return type;
	}


	/**
	 * Returns the data source minimum value
	 * @return the data source minValue
	 */
	public double getMinValue() {
		return minValue;
	}


	/**
	 * Returns the data source maximum value
	 * @return the data source maxValue
	 */
	public double getMaxValue() {
		return maxValue;
	}


	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSource other = (DataSource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("DataSource [name:")
			.append(name)
			.append(", type:")
			.append(type)
			.append(", min:")
			.append(minValue==Double.NaN ? "U" : minValue)
			.append(", min:")
			.append(maxValue==Double.NaN ? "U" : maxValue)
			.append("]")
			.toString();
	}
	
	
}
