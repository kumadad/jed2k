package org.jed2k;

import org.jed2k.data.PieceBlock;
import org.jed2k.protocol.Hash;
import java.util.ArrayList;
import java.util.Arrays;

public class DownloadingPiece {
    public enum BlockState {
        STATE_NONE((byte)0),
        STATE_REQUESTED((byte)1),
        STATE_WRITING((byte)2),
        STATE_FINISHED((byte)3);
        byte value;

        BlockState(byte b) {
            value = b;
        }
    }

    private class Block {
        public BlockState   state;
    }

    public int pieceIndex;
    private int blocksCount;
    public byte[] blockState;
    // TODO - for future usage cached values
    short requested;
    short writing;
    short finished;

    public DownloadingPiece(int pieceIndex, int blocksCount) {
        assert(pieceIndex >= 0);
        assert(blocksCount > 0);
        this.pieceIndex = pieceIndex;
        this.blocksCount = blocksCount;
        blockState = new byte[blocksCount];
        Arrays.fill(blockState, BlockState.STATE_NONE.value);
    }

    public int finishedCount() {
        int res = 0;
        for(int i = 0; i < blocksCount; ++i) {
            if (blockState[i] == BlockState.STATE_FINISHED.value) res++;
        }
        return res;
    }

    public int downloadingCount() {
        int res = 0;
        for(int i = 0; i < blocksCount; ++i) {
            if (blockState[i] == BlockState.STATE_REQUESTED.value) res++;
        }
        return res;
    }

    public int writingCount() {
        int res = 0;
        for(int i = 0; i < blocksCount; ++i)
            if (blockState[i] == BlockState.STATE_WRITING.value) res++;
        return res;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public void finishBlock(int blockIndex) {
        assert(blockIndex < blocksCount);
        blockState[blockIndex] = BlockState.STATE_FINISHED.value;
    }

    public void requestBlock(int blockIndex) {
        assert(blockIndex < blocksCount);
        blockState[blockIndex] = BlockState.STATE_REQUESTED.value;
    }

    public void writeBlock(int blockIndex) {
        assert(blockIndex < blocksCount);
        blockState[blockIndex] = BlockState.STATE_WRITING.value;
    }

    boolean isDownloaded(int blockIndex) {
        assert(blockIndex < blocksCount);
        return blockState[blockIndex] == BlockState.STATE_FINISHED.value ||
                blockState[blockIndex] == BlockState.STATE_WRITING.value;
    }

    boolean isFinished(int blockIndex) {
        assert(blockIndex < blocksCount);
        return blockState[blockIndex] == BlockState.STATE_FINISHED.value;
    }

    public void abortDownloading(int blockIndex) {
        blockState[blockIndex] = BlockState.STATE_NONE.value;
    }
}