
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

/**
 * Implementation of the Huffman algorithm to compress a text file to a binary file to potentially be 
 * transferred to another computer to then be decompressed with the same program. Uses a priority queue
 * with the ability to hold both an integer and an object in correct order. Can encode characters from
 * standard ascii 0 to 127 coding but could be changed easily for larger character libraries. * 
 * 
 * @author Jesse Dahir-Kanehl
 *
 */
public class HuffmanASCIICompression {

	class Node {
		Node left;
		char ch;
		Node right;

		Node(Node L, char c, Node r) {
			left = L;
			ch = c;
			right = r;
		}
	}

	Node root;
	int frequency[];
	int totalChars;
	PriorityQueue pq;
	String encodings[];
	
	// R is used as the number of chars in the ascii set
	static final int R = 128;
	// T is used as the nodes that connect leaves
	static final char T = (char)R; 

	public HuffmanASCIICompression() {
		frequency = new int[R];
		totalChars = 0;
		pq = new PriorityQueue(R);
		encodings = new String[R];
	}
				
	/**
	 * Encode reads in the text file, creates the frequency count, uses that to create the priority queue,
	 * builds the tree, creates the encodings, encodes the total characters and tree, and lastly encodes
	 * the text file out to our binary file.
	 * 
	 * @param infile Our text file to be copied
	 * @param outfile Our compressed binary file
	 * @throws IOException
	 */
	public void encode(String infile, String outfile)throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(infile));
		int a = r.read();
		while(a != -1) {
			createFrequency(a);
			a = r.read();
		}
		r.close();
		for (int i = 0; i < frequency.length; i++) {
			if (frequency[i] > 0 && !pq.full())
				pq.insert(frequency[i], new Node(null, (char)(i), null));
		}
		buildTree();
		createEncodings();
		BitOutputStream o = new BitOutputStream(outfile);
		o.writeInt(totalChars);
		StringBuilder tree = new StringBuilder(400);
		o.writeString(encodeTree(root, tree).toString());
		r = new BufferedReader(new FileReader(infile));
		a = r.read();
		while(a != -1) {
			o.writeBits(encodings[a]);
			a = r.read();
		}
		o.close();
		r.close();
	}
	
	// Adds one to the frequency for each character and to the total character count.
	private void createFrequency (int a) {
		frequency[a]++;
		totalChars++;
	}
	
	// Uses the priority queue to create a new tree out of the two minimum trees and
	//  the sum of their frequencies until only one tree is left which is assigned to root.
	private void buildTree () {
		while (!pq.empty()) {
			int sum = pq.getMinKey();
			Node left = (Node)pq.deleteMin();
			sum += pq.getMinKey();
			Node right = (Node)pq.deleteMin();
			Node n = new Node (left, T, right);
			pq.insert(sum, n);
		}
		root = (Node)pq.deleteMin();
	}

	private void createEncodings () {
		postOrder (root, "");
	}
	
	// To create the encoding we use a post order traversal of the tree.
	//	*Left branches are assigned a 0 and right branches a 1
	private void postOrder (Node r, String s) {
		if (r.ch == T) {
			postOrder (r.left, s + '0');
			postOrder(r.right, s + '1');
		}
		else {
			encodings[r.ch] = s;
		}
	}

	// Writes each node and leaf into one string in a recursive fashion.
	private StringBuilder encodeTree(Node r, StringBuilder s) throws IOException {
		if (r.ch == T) {
			encodeTree(r.left, s);
			encodeTree(r.right, s);
			return s.append(T);
		}
		else {
			return s.append(r.ch);
		}
	}
	
	/**
	 * To decode the compressed file the total characters are read from the first integer, the tree is recreated from the first string,
	 *  then the next bytes	are decoded to characters until the total number of characters from the original file are decoded.
	 * 
	 * @param infile The compressed binary file
	 * @param outfile The new text file which should be a copy of the first
	 * @throws IOException
	 */
	public void decode(String infile, String outfile)throws IOException {
		BitInputStream in = new BitInputStream(infile);
		BufferedWriter out = new BufferedWriter( new FileWriter (outfile));
		int tc = in.readInt();
		String s = in.readString();
		root = decodeTree(s);
		Node n = root;
		while (tc > 0) {
			if (n.ch != T) {
				out.write(n.ch);
				tc--;
				n = root;
			}
			else {
				if (in.readBit() == '0')
					n = n.left;
				else
					n = n.right;
			}
		}
		in.close();
		out.close();
	}

	// Decodes the tree in the same fashion as it was encoded until all leaves are discovered.
	private Node decodeTree(String s) throws IOException {
		char[] arr = s.toCharArray();
		Stack<Node> nodeStack = new Stack<>();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if (c != T)
				nodeStack.push(new Node(null, c, null));
			else {
				Node n = nodeStack.pop();
				nodeStack.push(new Node (nodeStack.pop(), T, n));
			}
		}
		return nodeStack.pop();
	}
	
	public static void main(String args[]) throws IOException {
		HuffmanASCIICompression h = new HuffmanASCIICompression();
		h.encode(args[0], args[1]);
		h.decode(args[1], args[0]+"_new");
	}
}