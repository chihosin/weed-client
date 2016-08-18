/*
 * Copyright (c) 2016 Lokra Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.lokra.seaweedfs.core.topology;

import java.util.List;

public class SystemClusterStatus {

    private MasterStatus leader;
    private List<MasterStatus> peers;
    private int total;
    private int available;

    public SystemClusterStatus(MasterStatus leader, List<MasterStatus> peers) {
        this.leader = leader;
        this.peers = peers;
        this.total = peers.size() + 1;
        this.available = 1;
        for (MasterStatus item : peers) {
            if (item.isActive()) {
                this.available++;
            }
        }
    }

    @Override
    public String toString() {
        return "SystemClusterStatus{" +
                "leader=" + leader +
                ", peers=" + peers +
                ", total=" + total +
                ", available=" + available +
                '}';
    }

    public MasterStatus getLeader() {
        return leader;
    }

    public List<MasterStatus> getPeers() {
        return peers;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailable() {
        return available;
    }
}
