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
package com.heliosapm.metrics.netty.collectd;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heliosapm.metrics.netty.collectd.event.CollectdEventsDecoder;
import com.heliosapm.metrics.netty.collectd.event.Event;
import com.heliosapm.metrics.netty.collectd.packet.CollectdPacketDecoder;

/**
 * <p>Title: Server</p>
 * <p>Description: Standalone server.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.heliosapm.metrics.netty.collectd.Server</code></p>
 */

public class Server implements ChannelPipelineFactory, ChannelUpstreamHandler {
	/** Instance logger */
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/** The listening port */
	private int port = 25826;
	/** The binding interface */
	private String iface = "0.0.0.0"; //"127.0.0.1";
	/** Indicates if multicast should be enabled */
	private boolean enableBroadcast = false;
	
	private ConnectionlessBootstrap bootstrap = null;
	private NioDatagramChannelFactory channelFactory = null;
	private ChannelPipelineFactory pipelineFactory = this;
	private InetSocketAddress isock = null;
	private final ThreadFactory tf = new ThreadFactory() {
		final AtomicInteger serial = new AtomicInteger(0);
		@Override
		public Thread newThread(final Runnable r) {
			final Thread t = new Thread(r, "DatagramServerThread#" + serial.incrementAndGet());
			return t;
		}		
	};
	final ExecutorService ex = Executors.newCachedThreadPool(tf);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		System.setProperty("java.net.preferIPv4Stack", "true");
		final Server server = new Server();
		server.start();

	}
	
	
	public void start() {
		try {
			isock = new InetSocketAddress(iface, port);
			log.info("Starting Server on [{}]", isock);
			channelFactory = new NioDatagramChannelFactory(ex);
			bootstrap = new ConnectionlessBootstrap(channelFactory);
			bootstrap.setPipelineFactory(pipelineFactory);
			bootstrap.setOption("receiveBufferSizePredictorFactory",
					new FixedReceiveBufferSizePredictorFactory(1024));
			bootstrap.bind(isock);
			log.info("Server Started");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	public void stop() {
		
	}


	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = Channels.pipeline();
		//pipeline.addLast("Logging", new LoggingHandler(Server.class, InternalLogLevel.INFO, true));
		pipeline.addLast("PacketDecoder", new CollectdPacketDecoder());
		pipeline.addLast("EventDecoder", new CollectdEventsDecoder());
		pipeline.addLast("EventHandler", this);
		return pipeline;
	}
	
	public void onEvents(final Event...events) {
		for(Event e: events) {
			log.info(e.toString());
		}
	}


	@Override
	public void handleUpstream(final ChannelHandlerContext ctx, final ChannelEvent e) throws Exception {
		if(e instanceof MessageEvent) {
			final MessageEvent me = (MessageEvent)e;
			final Object msg = me.getMessage();
			if(msg!=null && (msg instanceof Event[])) {
				final Event[] events = (Event[])msg;
				onEvents(events);
				
				return;
			}
		}
		ctx.sendUpstream(e);
	}

}
