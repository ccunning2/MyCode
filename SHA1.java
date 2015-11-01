package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SHA1 {




    public static void main(String[] args) {

        String mess = readFile(args[0]);



        System.out.println(process(mess));



    }

    public static String process(String message){
        byte[] answer = preProcess(message);
        ArrayList<ArrayList<Byte>> chunks = getChunks(answer);
       return mainLoop(chunks);
    }

    // http://stackoverflow.com/questions/1656797/how-to-read-a-file-into-string-in-java
    public static String readFile(String path) {
        StringBuilder fileData = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            char[] buf = new char[1024];
            int numRead = 0;
            while((numRead = reader.read(buf)) != -1){
                   fileData.append(String.valueOf(buf,0,numRead));
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert reader != null;
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileData.toString();
    }


    //Return an arraylist of an arraylist of 64 byte chunks
    public static ArrayList<ArrayList<Byte>> getChunks(byte[] answer) {
        ArrayList<ArrayList<Byte>> chunks = new ArrayList<ArrayList<Byte>>();
        ArrayList<Byte> temp = new ArrayList<Byte>();

        int counter = 0;
        for(int i=0; i< answer.length; i++){
            temp.add(answer[i]);
            counter++;
            if(counter == 64){
                chunks.add(temp);
                temp = new ArrayList<Byte>();
                counter = 0;
            }
        }

        return chunks;
    }


    //Returns a byte array, length padded to be a multiple of 512
    public static byte[] preProcess(String message){
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        long m1 = message.length() * 8; //Message length, in number of bits
        for(int i=0; i<message.length(); i++){ //Add every char in message to byte arraylist
            byte temp = (byte) message.charAt(i);
            bytes.add(temp);
        }

        byte one = (byte) 0x80;
        byte zero = 0x00;

        //append the bit '1' to the message
        bytes.add(one);

        //append 0's until message length in bits is congruent to 448 mod 512 (56 mod 64 in bytes)
        while (bytes.size() % 64 != 56){
            bytes.add(zero);
        }



        //Now need to append m1
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(m1);

        for(int i=0; i<8; i++){
            bytes.add(byteBuffer.get(i));
        }


        byte[] byteArray = new byte[bytes.size()];

        for (int i = 0; i< bytes.size(); i++){
            byteArray[i] = bytes.get(i);
        }

        return byteArray;


    }

    private static String mainLoop(ArrayList<ArrayList<Byte>> chunks) {
       int h0 = 0x67452301;
       int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;
        for (ArrayList<Byte> chunk : chunks){
            //Break chunk into 16 32-bit (4 byte) big endian words
            int[] words = get16Words(chunk);
            //Extend sixteen 32-bit words into eighty 32-bit words
            for (int i= 16; i<80; i++){
                words[i] = (words[i-3] ^ words[i-8] ^ words[i-14] ^ words[i-16]);
                words[i] = Integer.rotateLeft(words[i], 1);
            }

            //Init hash value for this chunk:
            int a = h0;
            int b = h1;
            int c = h2;
            int d = h3;
            int e = h4;

            int f = 0;
            int k = 0;
            //Main loop:
            for (int i = 0; i< 80; i++){
                if (i >= 0 && i <= 19){
                    f = (b & c) | ((~b) & d);
                    k = 0x5a827999;
                } else if (i >= 20 && i <= 39){
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                } else if (i >=40 && i <= 59){
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                } else if (i >= 60 && i <= 79){
                    f = (b ^ c ^ d);
                    k = 0xCA62C1D6;
                }

                int temp = Integer.rotateLeft(a, 5) + f + e + k + words[i];
                e = d;
                d = c;
                c = Integer.rotateLeft(b,30);
                b = a;
                a = temp;
            }
            //Add chunk's hash to result so far
            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;

        }
        String finalValue = computeFinalHashValue(h0,h1,h2,h3,h4);
        return finalValue;

    }

    private static String computeFinalHashValue(int h0, int h1, int h2, int h3, int h4) {
        //Convenient to have h0-h4 as byte arrays


        byte[] h_0 = ByteBuffer.allocate(4).putInt(h0).array();
        byte[] h_1 = ByteBuffer.allocate(4).putInt(h1).array();
        byte[] h_2 = ByteBuffer.allocate(4).putInt(h2).array();
        byte[] h_3 = ByteBuffer.allocate(4).putInt(h3).array();
        byte[] h_4 = ByteBuffer.allocate(4).putInt(h4).array();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(h_0);
            outputStream.write(h_1);
            outputStream.write(h_2);
            outputStream.write(h_3);
            outputStream.write(h_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] hash =  outputStream.toByteArray();

        StringBuilder sb = new StringBuilder();
        for( int i=0; i< hash.length; i++){
            sb.append(String.format("%02x", hash[i] & 0xff));
        }
        return sb.toString();
    }


    //Gets 16 32-bit big endian words in position 0-15 of array. Extends these 16 words into 80 words
    public static int[] get16Words(ArrayList<Byte> chunk) {
        int[] sixteenWords = new int[80];
        int index = 0;
        for(int i=0; i< 16; i++){
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.put(chunk.get(index));
            index++;
            byteBuffer.put(chunk.get(index));
            index++;
            byteBuffer.put(chunk.get(index));
            index++;
            byteBuffer.put(chunk.get(index));
            index++;
            byteBuffer.position(0);
            sixteenWords[i] = byteBuffer.getInt();
        }
        return sixteenWords;
    }




}
