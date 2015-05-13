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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * <p>Title: DataSet</p>
 * <p>Description: Represents a dataset entry in a collectd <b>types.db</b> file.
 * See <a href="http://collectd.org/documentation/manpages/types.db.5.shtml">Manpage types.db(5)</a></p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.type.DataSet</code></p>
 */

public class DataSet {
	/** The data set name */
	final String name;
	/** The data set's data sources */
	final Map<String, DataSource> dataSources;
	
	/** A white space splitter expression */
	static final Pattern WHITESPACE_SPLITTER = Pattern.compile("\\s+");
	/** A comma space splitter expression */
	static final Pattern COMMA_SPLITTER = Pattern.compile(",");
	
	/** A cache of DataSets keyed by the constructing entry text */
	private static final Map<String, DataSet> dataSets = new ConcurrentHashMap<String, DataSet>(256);
	
	/**
	 * Acquires the data set from a string in the format of an entry in a types.db file
	 * @param entry The types.db text line to initialize the data set from 
	 * @return the data set
	 */
	public static DataSet init(final String entry) {
		if(entry==null || entry.trim().isEmpty()) throw new IllegalArgumentException("The passed DataSet definition text was null or empty");
		final String[] frags = WHITESPACE_SPLITTER.split(entry.trim());
		if(frags.length<2) throw new IllegalArgumentException("The passed DataSet definition was invalid [" + entry + "]. Did not have >= 2 segments.");
		final String key = frags[0];
		DataSet dataSet = dataSets.get(key);
		if(dataSet==null) {
			synchronized(dataSets) {
				dataSet = dataSets.get(key);
				if(dataSet==null) {
					dataSet = new DataSet(entry);
					dataSets.put(dataSet.name, dataSet);
				}
			}
		}
		return dataSet;
	}
	
	/**
	 * Returns the named DataSet
	 * @param name The name of the data set to retrieve
	 * @return the named DataSet or null if one was not found
	 */
	public static DataSet get(final String name) {
		return dataSets.get(name);
	}
	
	/**
	 * Creates a new DataSet
	 * @param entry The data set definition text
	 */
	private DataSet(final String entry) {
		if(entry==null || entry.trim().isEmpty()) throw new IllegalArgumentException("The passed DataSet definition text was null or empty");
		final String[] frags = WHITESPACE_SPLITTER.split(entry.trim());
		if(frags.length<2) throw new IllegalArgumentException("The passed DataSet definition was invalid [" + entry + "]. Did not have >= 2 segments.");
		name = frags[0];
		final String[] dses = COMMA_SPLITTER.split(WHITESPACE_SPLITTER.matcher(entry.substring(name.length())).replaceAll(""));
		Map<String, DataSource> _dataSources = new LinkedHashMap<String, DataSource>(dses.length);
		
		for(int i = 0; i < dses.length; i++) {
			dses[i] = dses[i].trim();
			if(dses[i].isEmpty()) throw new IllegalArgumentException("The passed DataSet definition was invalid [" + entry + "]. DS Segments [" + i + "] was empty");
			DataSource ds = DataSource.getDataSource(dses[i]);
			_dataSources.put(ds.getName(), ds);
		}
		dataSources = Collections.unmodifiableMap(_dataSources);
	}
	
	
	
	/**
	 * Returns the DataSet name
	 * @return the DataSet name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a copy of the DataSources array
	 * @return a copy of the DataSources array
	 */
	public DataSource[] getDataSources() {
		return dataSources.values().toArray(new DataSource[dataSources.size()]);
	}
	
	/**
	 * Returns the number of data sources
	 * @return the number of data sources
	 */
	public int getDataSourceCount() {
		return dataSources.size();
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSources == null) ? 0 : dataSources.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DataSet other = (DataSet) obj;
		if (dataSources == null) {
			if (other.dataSources != null)
				return false;
		} else if (!dataSources.equals(other.dataSources))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("DataSet [name=");
		builder.append(name);
		builder.append(", dataSources=");
		builder.append(dataSources != null ? toString(dataSources.entrySet(), maxLen) : null);
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
