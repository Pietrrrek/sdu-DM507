package huffmanconverter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author me
 */
public class Decode {
    /**
     * Decodes a file that has been encoded using Huffman encoding
     * File should first have a byte frequency table
     * Then have the file content in Huffman encoding
     * @param inp The input to decode
     * @param outp The output to write the decoded input to
     */
    private static void huffmanDecode(BitInputStream inp, OutputStream outp) throws IOException {
        // first read our byte table from the file
        int[] byteTable = new int[256];
        // long used so we can decode filesizes of a couple exabytes instead of ~2 GB with int
        long numBytes = 0;
        for (int i = 0; i < byteTable.length; ++i) {
            byteTable[i] = inp.readInt();
            numBytes += byteTable[i];
        }
        
        // then convert that to a Huffman tree
        Huffman.Node tree = Huffman.generateTree(byteTable);
        
        // then decode the file
        long decodedBytes = 0;
        while (decodedBytes < numBytes) {
            outp.write(decodeByte(inp, tree));
            ++decodedBytes;
        }
    }
    
    /**
     * Decodes a single byte from an input, according to the given Huffman tree
     * @param inp The input to decode a byte from
     * @param tree The Huffman tree to use for decoding
     */
    private static int decodeByte(BitInputStream inp, Huffman.Node tree) throws IOException {
        Huffman.Node curTree = tree;
        // keep going until we reach a leaf in the tree
        while (true) {
            int bit = inp.readBit();
            Huffman.Node child = bit == 0
                    ? curTree.left
                    : curTree.right;
            // we know the child is either a new subtree or a leaf
            // if it's a leaf, it's the byte we decoded into
            if (child.isLeaf()) {
                return child.getByte();
            } else {
                curTree = child;
            }
        }
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 1) {
            System.out.println("\u001B[31m[Err] No input given\nUsage:\n  java Decode in.enc [out.txt]");
            return;
        }
        if (args.length < 2) {
            System.err.println("\u001B[33m[Warn] No output given; writing to stdout");
        }
        
        // initialize our input and output streams
        BitInputStream inp = new BitInputStream(
                new BufferedInputStream(new FileInputStream(args[0]))
        );
        OutputStream outp = args.length < 2
                ? System.out
                : new BufferedOutputStream(new FileOutputStream(args[1]));
        
        // then decode our input and write it to our output
        huffmanDecode(inp, outp);
        
        // then close our streams
        inp.close();
        outp.close();
    }
}
