

import java.io.*;

/**
 * This class writes out ints, strings, and bits in a space efficient way.
 * 
 * @author Jesse Dahir-Kanehl
 *
 */
public class BitOutputStream {
	DataOutputStream out;
	private StringBuilder strBld;

	public BitOutputStream(String filename) throws IOException {
		out = new DataOutputStream(new FileOutputStream(filename));
		strBld = new StringBuilder(136);
		//more initialization is needed
	}

	public void writeInt(int i)  throws IOException {
		out.writeInt(i);
	}

	public void writeString(String s) throws IOException {
		out.writeUTF(s);
	}
	
	/*
	 *  writeBit uses a StringBuilder as a buffer so that it doesn't write if the string is less than 8 bits
	 *  it doens't waste any space and waits until at least 8 bits of information come though before a byte
	 *  is written out.
	 */
	public void writeBits(String s)  throws IOException {
		strBld.append(s);
		while (strBld.length() >= 8) {
			int sum = 0;
			for (int j = 0; j < 7; j++) {
				char c = strBld.charAt(0); 
				strBld.deleteCharAt(0);
				sum += (c - '0');
				sum *= 2;
			}
			char c = strBld.charAt(0); 
			strBld.deleteCharAt(0);
			sum += (c - '0');
			out.writeByte(sum);
		}
	}

	//If their is anything left in the StringBuilder buffer it adds a padding of zeros and prints it out
	public void close()  throws IOException {
		while (strBld.length() < 8) {
			strBld.append('0');
		}
		int sum = 0;
		for (int j = 0; j < 7; j++) {
			char c = strBld.charAt(0); 
			strBld.deleteCharAt(0);
			sum += (c - '0');
			sum *= 2;
		}
		char c = strBld.charAt(0); 
		strBld.deleteCharAt(0);
		sum += (c - '0');
		out.writeByte(sum);
		out.close();
	}
}
