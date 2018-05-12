/*
 * Written by Johan Fagerberg
 */
package huffmanconverter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author me
 */
public class Encode {    
    /**
     * Encodes a file according to Huffman encoding
     * First outputs the byte table for later decoding
     * Then outputs the input content
     * @param inpFilename The input file to encode
     * @param outp The output to write the encoded input to
     * @param byteTable The # of occurrences of each byte
     */
    private static void huffmanEncode(
            String inpFilename, BitOutputStream outp) throws IOException {
        // first read our byte table from our input
        int[] byteTable = readByteTable(generateInputStream(inpFilename));
        // then generate our byte -> bit sequence table
        String[] lookupTable = Huffman.generate(byteTable);
        
        // then write our output
        // first our byte table for later decoding
        for (int i : byteTable) {
            outp.writeInt(i);
        }
        // then our encoded file
        InputStream inp = generateInputStream(inpFilename);
        int data;
        while ((data = inp.read()) != -1) {
            for (char c : lookupTable[data].toCharArray()) {
                outp.writeBit(c == '0' ? 0 : 1);
            }
        }
        
        // close our resources
        inp.close();
        outp.close();
    }
    
    /**
     * Reads a file, returning an int[256] of occurrences of each byte
     * @param input The input to read
     * @return An int[256] with each value being the # of occurrences of the byte
     */
    private static int[] readByteTable(InputStream input) throws IOException {
        int[] byteTable = new int[256];
        int data;
        while ((data = input.read()) != -1) {
            ++byteTable[data];
        }
        return byteTable;
    }
    
    /**
     * Generates an InputStream from a filename
     * Moved to its own method because it's done twice,
     *   and some testing was done to optimize it
     * @param inpFilename File to create InputStream from
     * @return A BufferedInputStream(FileInputStream) of the file
     */
    private static InputStream generateInputStream(String inpFilename)
            throws FileNotFoundException {
        return new BufferedInputStream(
                new FileInputStream(inpFilename)
        );
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 1) {
            System.out.println("\u001B[31m[Err] No input given\nUsage:\n  java Encode in.txt [out.enc]");
            return;
        }
        if (args.length < 2) {
            System.err.println("\u001B[33m[Warn] No output given; writing to stdout");
        }
        
        // initialize our output stream
        // we cannot initialize our input stream yet, since FileInputStream 
        //    doesn't support .reset.
        //    we must delay creating it until we're inside our encode method
        BitOutputStream outp = new BitOutputStream(
                args.length < 2
                    ? System.out
                    : new BufferedOutputStream(new FileOutputStream(args[1]))
        );
        
        // then encode our input and write it to our output
        huffmanEncode(args[0], outp);
    }
}
