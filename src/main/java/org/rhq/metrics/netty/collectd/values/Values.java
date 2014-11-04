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

package org.rhq.metrics.netty.collectd.values;

import java.util.List;

/**
 * @author Thomas Segismont
 */
public final class Values {
    private final List<Sample> samples;

    public Values(List<Sample> samples) {
        this.samples = samples;
    }

    public List<Sample> getSamples() {
        return samples;
    }

    @Override
    public String toString() {
        return "Values[" + "samples=" + samples + ']';
    }
}
