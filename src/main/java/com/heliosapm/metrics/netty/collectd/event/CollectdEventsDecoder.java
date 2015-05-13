/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heliosapm.metrics.netty.collectd.event;

import static com.heliosapm.metrics.netty.collectd.event.TimeResolution.HIGH_RES;
import static com.heliosapm.metrics.netty.collectd.event.TimeResolution.SECONDS;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

import com.heliosapm.metrics.netty.collectd.packet.CollectdPacket;
import com.heliosapm.metrics.netty.collectd.packet.NumericPart;
import com.heliosapm.metrics.netty.collectd.packet.Part;
import com.heliosapm.metrics.netty.collectd.packet.PartType;
import com.heliosapm.metrics.netty.collectd.packet.StringPart;
import com.heliosapm.metrics.netty.collectd.packet.ValuePart;
import com.heliosapm.metrics.netty.collectd.packet.Values;

/**
 * <p>Title: CollectdEventsDecoder</p>
 * <p>Description: </p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Thomas Segismont
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.event.CollectdEventsDecoder</code></p>
 */
@ChannelHandler.Sharable
public final class CollectdEventsDecoder extends OneToOneDecoder {
    
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(CollectdEventsDecoder.class);

		/**
		 * {@inheritDoc}
		 * @see org.jboss.netty.handler.codec.oneone.OneToOneDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
		 */
		@Override
		protected Event[] decode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {
			if(msg==null) {
				return null;
			}
			final CollectdPacket[] packets;  //(CollectdPacket)msg;

			if(msg instanceof CollectdPacket[]) {
				packets = (CollectdPacket[])msg;
			} else if(msg instanceof CollectdPacket) {
				packets = new CollectdPacket[]{(CollectdPacket)msg};
			} else {
				return null;
			}
			
			final List<Event> events = new ArrayList<Event>(50);
      long start = System.currentTimeMillis();
      String host = null, pluginName = null, pluginInstance = null, typeName = null, typeInstance = null;
      TimeSpan timestamp = null, interval = null;
      for(final CollectdPacket packet: packets) {
	      for (final Part<?> part : packet.getParts()) {
	          final PartType partType = part.getPartType();
	          switch (partType) {
	          case HOST:
	              host = getString(part);
	              break;
	          case PLUGIN:
	              pluginName = getString(part);
	              break;
	          case PLUGIN_INSTANCE:
	              pluginInstance = getString(part);
	              break;
	          case TYPE:
	              typeName = getString(part);
	              break;
	          case INSTANCE:
	              typeInstance = getString(part);
	              break;
	          case TIME:
	              timestamp = new TimeSpan(getLong(part), SECONDS);
	              break;
	          case TIME_HIGH_RESOLUTION:
	              timestamp = new TimeSpan(getLong(part), HIGH_RES);
	              break;
	          case INTERVAL:
	              interval = new TimeSpan(getLong(part), SECONDS);
	              break;
	          case INTERVAL_HIGH_RESOLUTION:
	              interval = new TimeSpan(getLong(part), HIGH_RES);
	              break;
	          case VALUES:
	              Number[] values = getValues(part).getData();
	              ValueListEvent event = new ValueListEvent(host, timestamp, pluginName, pluginInstance, typeName,
	                  typeInstance, values, interval);
	              
	              logger.debug("Decoded ValueListEvent: " + event);
	              events.add(event);
	              break;
	          default:
	              logger.debug("Skipping unknown part type: " + partType);
	          }
	      }
      }
      if (logger.isDebugEnabled()) {
          long stop = System.currentTimeMillis();
          logger.debug("Decoded events in " + (stop - start) + " ms");
      }

      return events.toArray(new Event[0]);
		}
	

    private String getString(final Part<?> part) {
        return ((StringPart) part).getValue();
    }

    private Long getLong(final Part<?> part) {
        return ((NumericPart) part).getValue();
    }

    private Values getValues(final Part<?> part) {
        return ((ValuePart) part).getValue();
    }
}
