import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class reads ints, strings, and bits from a Bit Output Stream.
 * 
 * @author Jesse Dahir-Kanehl
 *
 */
public class BitInputStream {
	DataInputStream in;
	StringBuffer sb;
	
	public BitInputStream(String infile)  throws IOException {
		in = new DataInputStream(new FileInputStream(infile));
		sb = new StringBuffer();
		//more initialization will be required
	}

	public int readInt()  throws IOException {
		return in.readInt();
	}

	public String readString() throws IOException  {
		return in.readUTF();
	}

	//Reads in a byte at a time and keeps a buffer to only output a bit.
	public int readBit()  throws IOException {
		if (sb.length() == 0) {
			int a = in.readUnsignedByte();
			sb.append(Integer.toBinaryString(a));
			while (sb.length() < 8) {
				sb.insert(0, '0');
			}
		}
		char c = sb.charAt(0); 
		sb.deleteCharAt(0);
		return c;
		
	}

	public void close() throws IOException {
		in.close();
	}
}
		
		