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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>Title: DataSet</p>
 * <p>Description: Represents a dataset entry in a collectd <b>types.db</b> file.
 * See <a href="http://collectd.org/documentation/manpages/types.db.5.shtml">Manpage types.db(5)</a></p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.rhq.metrics.netty.collectd.type.DataSet</code></p>
 */

public class DataSet {
	final String name;
	final Map<String, DataSource> dataSources;
	
	/** A white space splitter expression */
	static final Pattern WHITESPACE_SPLITTER = Pattern.compile("\\s+");
	/** A comma space splitter expression */
	static final Pattern COMMA_SPLITTER = Pattern.compile(",");
	
	/**
	 * Creates a new DataSet
	 * @param entry The data set definition text
	 */
	public DataSet(final String entry) {
		if(entry==null || entry.trim().isEmpty()) throw new IllegalArgumentException("The passed DataSet definition text was null or empty");
		final String[] frags = WHITESPACE_SPLITTER.split(entry.trim());
		if(frags.length<2) throw new IllegalArgumentException("The passed DataSet definition was invalid [" + entry + "]. Did not have >= 2 segments.");
		name = frags[0];
		final String[] dses = COMMA_SPLITTER.split(entry.substring(entry.trim().indexOf(" ")).trim());
		Map<String, DataSource> _dataSources = new HashMap<String, DataSource>(dses.length);
		
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
	
}
