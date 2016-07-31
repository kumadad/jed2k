package org.jed2k;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.jed2k.protocol.Hash;
import org.jed2k.protocol.NetworkIdentifier;
import org.jed2k.data.PieceBlock;

public class Transfer {
    private Hash hash;
    private Set<NetworkIdentifier> sources = new TreeSet<NetworkIdentifier>();
    private long size;
    private ArrayList<Hash> hashset;
    private Statistics stat = new Statistics();
    private PiecePicker picker;
    private Session session;

    public Transfer(Session s, final AddTransferParams atp) {
        assert(s != null);
        assert(hash != null);
        assert(size != 0);
        this.hash = atp.hash;
        this.size = atp.size.longValue();
        this.hashset = null;
        session = s;
    }

    Hash hash() {
        return hash;
    }

    public long size() {
        return this.size;
    }

    public boolean validateHashset(Collection<Hash> hashset) {
        if (this.hashset != null) {
            // compare
        } else {
            return hash.equals(Hash.fromHashSet(hashset));
        }

        return true;
    }

    public PieceBlock requestBlock() {
        return new PieceBlock(0,0);
    }

    public void append(PeerConnection connection) {

    }

    void setupSources(Collection<NetworkIdentifier> sources) {
        for(NetworkIdentifier entry: sources) {
            if (!this.sources.contains(entry)) {
                this.sources.add(entry);
                // process new source
            }
        }
    }

	void secondTick(long currentSessionTime) {
        // TODO Auto-generated method stub
        stat.secondTick(currentSessionTime);
        // TODO - add statistics from all peed connections
    }

    public Statistics statistics() {
        return stat;
    }

    public PiecePicker getPicker() {
        return picker;
    }

    void abort() {

    }
}
