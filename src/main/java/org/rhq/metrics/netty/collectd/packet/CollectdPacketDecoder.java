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

package org.rhq.metrics.netty.collectd.packet;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.rhq.metrics.netty.collectd.event.DataType;

/**
 * A Netty decoding handler: from {@link DatagramPacket} to {@link CollectdPacket}.
 *
 * @author Thomas Segismont
 */
@Sharable
public final class CollectdPacketDecoder extends OneToOneDecoder {
	
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(CollectdPacketDecoder.class);

    @Override
    protected CollectdPacket decode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {
      final long start = System.currentTimeMillis();
      if(msg==null) return null;
      if(!(msg instanceof ChannelBuffer)) return null;
      //final DatagramPacket packet = (DatagramPacket)msg;
      final ChannelBuffer content = (ChannelBuffer)msg; //ChannelBuffers.wrappedBuffer(packet.getData());
      final List<Part<?>> parts = new ArrayList<Part<?>>(100);
//      final List<CollectdPacket> packets = new ArrayList<CollectdPacket>(10);
      for (;;) {
        if (!hasReadableBytes(content, 4)) {
            break;
        }
        short partTypeId = content.readShort();
        PartType partType = PartType.findById(partTypeId);
        int partLength = content.readUnsignedShort();
        int valueLength = partLength - 4;
        if (!hasReadableBytes(content, valueLength)) {
            break;
        }
        if (partType == null) {
            content.skipBytes(valueLength);
            continue;
        }
        Part<?> part;
        switch (partType) {
        case HOST:
        case PLUGIN:
        case PLUGIN_INSTANCE:
        case TYPE:
        case INSTANCE:
            part = new StringPart(partType, readStringPartContent(content, valueLength));
            break;
        case TIME:
        case TIME_HIGH_RESOLUTION:
        case INTERVAL:
        case INTERVAL_HIGH_RESOLUTION:
            part = new NumericPart(partType, readNumericPartContent(content));
            break;
        case VALUES:
            part = new ValuePart(partType, readValuePartContent(content, valueLength));
            break;
        default:
            part = null;
            content.skipBytes(valueLength);
        }
        //noinspection ConstantConditions
        if (part != null) {
            logger.debug("Decoded part: " + part);
            parts.add(part);
        }
    }

    if (logger.isDebugEnabled()) {
        long stop = System.currentTimeMillis();
        logger.debug("Decoded datagram in " + (stop - start) + " ms");
    }

    if (parts.size() > 0) {
        return new CollectdPacket(parts.toArray(new Part[parts.size()]));
//        out.add(collectdPacket);
    } else {
        logger.debug("No parts decoded, no CollectdPacket output");
    }

    	
    	return null;
    }
    

    private boolean hasReadableBytes(final ChannelBuffer content, int count) {
        return content.readableBytes() >= count;
    }

    private String readStringPartContent(final ChannelBuffer content, int length) {
        String string = content.toString(content.readerIndex(), length - 1 /* collectd strings are \0 terminated */,
            CharsetUtil.US_ASCII);
        content.skipBytes(length); // the previous call does not move the readerIndex
        return string;
    }

    private long readNumericPartContent(final ChannelBuffer content) {
        return content.readLong();
    }
    
    private static long swapLong(final long value) {
    	return Long.reverseBytes(value);
    }    

    private Values readValuePartContent(final ChannelBuffer content, int length) {
        int beginIndex = content.readerIndex();
        int total = content.readUnsignedShort();
        DataType[] dataTypes = new DataType[total];
        for (int i = 0; i < total; i++) {
            byte sampleTypeId = content.readByte();
            dataTypes[i] = DataType.findById(sampleTypeId);
        }
        Number[] data = new Number[total];
        for (int i = 0; i < total; i++) {
            DataType dataType = dataTypes[i];
            switch (dataType) {
            case COUNTER:
            case ABSOLUTE:
                byte[] valueBytes = new byte[8];
                content.readBytes(valueBytes);
                data[i] = new BigInteger(1, valueBytes);
                break;
            case DERIVE:
                data[i] = content.readLong();
                break;
            case GAUGE:
                data[i] = Double.longBitsToDouble(swapLong(content.readLong()));
                break;
            default:
                logger.debug("Skipping unknown data type: " + dataType);
            }
        }
        // Skip any additionnal bytes
        int readCount = content.readerIndex() - beginIndex;
        if (length > readCount) {
            content.skipBytes(readCount - length);
        }
        return new Values(dataTypes, data);
    }
}
