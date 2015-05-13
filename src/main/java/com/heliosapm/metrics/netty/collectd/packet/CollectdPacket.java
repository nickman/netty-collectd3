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

package com.heliosapm.metrics.netty.collectd.packet;

import static com.heliosapm.metrics.netty.collectd.util.Assert.assertNotNull;

import java.util.Arrays;

/**
 * A collectd <a href="https://collectd.org/wiki/index.php/Binary_protocol#Protocol_structure">packet</a>, composed of
 * one or more "parts".
 *
 * @author Thomas Segismont
 */
public final class CollectdPacket {
    private final Part[] parts;

    public CollectdPacket(Part[] parts) {
        assertNotNull(parts, "parts is null");
        this.parts = parts;
    }

    public Part[] getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return "Packet[" + "parts=" + Arrays.asList(parts) + ']';
    }
}
