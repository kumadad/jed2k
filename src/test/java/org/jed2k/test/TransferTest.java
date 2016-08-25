package org.jed2k.test;

import org.jed2k.*;
import org.jed2k.data.PieceBlock;
import org.jed2k.exception.JED2KException;
import org.jed2k.protocol.Hash;
import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ap197_000 on 25.08.2016.
 */
public class TransferTest {

    @Test
    public void testBytesDonePartialBlock() throws JED2KException {
        long fileSize = Constants.PIECE_SIZE*3 + Constants.BLOCK_SIZE*2 + 334;  // 4 pieces and 3 blocks in last piece
        PiecePicker picker = new PiecePicker(4, 3);
        Transfer t = new Transfer(new AddTransferParams(Hash.EMULE, fileSize, "", true), picker);
        picker.restoreHave(0);
        picker.restoreHave(1);

        // move blocks to downloading queue
        LinkedList<PieceBlock> pieces = new LinkedList<>();
        picker.pickPieces(pieces, Constants.BLOCKS_PER_PIECE + 3);
        assertEquals(Constants.BLOCKS_PER_PIECE + 3, pieces.size());

        // piece 2 four blocks are downloaded
        picker.markAsDownloading(new PieceBlock(2, 0));
        picker.markAsDownloading(new PieceBlock(2, 1));
        picker.markAsDownloading(new PieceBlock(2, 2));
        picker.markAsFinished(new PieceBlock(2, 3));
        picker.markAsFinished(new PieceBlock(2, 4));
        picker.markAsWriting(new PieceBlock(2, 5));
        picker.markAsDownloading(new PieceBlock(2, 40));
        picker.markAsDownloading(new PieceBlock(2, 34));
        picker.markAsWriting(new PieceBlock(2, 49));

        // piece 3 two blocks are downloaded
        picker.markAsDownloading(new PieceBlock(3, 0));
        picker.markAsWriting(new PieceBlock(3, 1));
        picker.markAsFinished(new PieceBlock(3, 2));

        TransferStatus status = new TransferStatus();
        t.getBytesDone(status);
        assertEquals(Constants.PIECE_SIZE*2 + Constants.BLOCK_SIZE*4 + Constants.BLOCK_SIZE + 334, status.totalDone);
    }

    @Test
    public void testBytesDonePartialPiece() throws JED2KException {
        long fileSize = Constants.PIECE_SIZE - 1024;
        PiecePicker picker = new PiecePicker(1, Constants.BLOCKS_PER_PIECE - 1);
        Transfer t = new Transfer(new AddTransferParams(Hash.EMULE, fileSize, "", true), picker);
        picker.restoreHave(0);
        TransferStatus status = new TransferStatus();
        t.getBytesDone(status);
        assertEquals(Constants.PIECE_SIZE - 1024, status.totalDone);
    }

    @Test
    public void testBytesDoneSparse() throws JED2KException {
        long fileSize = Constants.PIECE_SIZE*2 - 1024;
        PiecePicker picker = new PiecePicker(2, Constants.BLOCKS_PER_PIECE);
        Transfer t = new Transfer(new AddTransferParams(Hash.EMULE, fileSize, "", true), picker);

        // we have 1 piece(last) without 1024 bytes + 2 blocks in first piece
        picker.restoreHave(1);
        LinkedList<PieceBlock> pieces = new LinkedList<>();
        picker.pickPieces(pieces, 3);
        picker.markAsDownloading(new PieceBlock(0, 0));
        picker.markAsFinished(new PieceBlock(0, 49));
        picker.markAsWriting(new PieceBlock(0, 48));

        TransferStatus status = new TransferStatus();
        t.getBytesDone(status);
        assertEquals(Constants.PIECE_SIZE - 1024 + Constants.BLOCK_SIZE*2, status.totalDone);
    }
}
