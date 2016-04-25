import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Problem2 {
	private final static int KEYSIZEBIT = 32; // 4 byte
	private final static int KEYSIZEBYTE = KEYSIZEBIT/8;
	private final static int BLOCKSIZEBIT = 48; //6 byte
	private final static int BLOCKSIZEBYTE = BLOCKSIZEBIT/8;
	private final static int NUMOFITERATE = 16777216;
	public static void main(String[] args){
		byte[] plaintext = new byte[BLOCKSIZEBYTE];
		byte[] knownCiphertext = new byte[BLOCKSIZEBYTE];
		Nex1 nex1 = new Nex1();
		
		/** MAKE PLAINTEXT  **/
		plaintext[0] = 0x01;
		plaintext[1] = 0x23;
		plaintext[2] = 0x45;
		plaintext[3] = 0x67;
		plaintext[4] = (byte)0x89;
		plaintext[5] = (byte)0xab;
		
		BigInteger bplaintext = new BigInteger(plaintext);
		long lplaintext = bplaintext.longValue();
		/** MAKE KNOWN CIPHERTEXT **/
		
		knownCiphertext[0] = (byte)0xb2;
		knownCiphertext[1] = (byte)0x31;
		knownCiphertext[2] = (byte)0xbb;
		knownCiphertext[3] = (byte)0x5a;
		knownCiphertext[4] = (byte)0x65;
		knownCiphertext[5] = (byte)0x8b;
		
		BigInteger bknownCiphertext = new BigInteger(knownCiphertext);
		long lknownCiphertext = bknownCiphertext.longValue();
		
		long[] lmeddleCipherFromEnc = new long[NUMOFITERATE];
		long[] lmeddleCipherFromDec = new long[NUMOFITERATE];
		long[] lmeddleCipherFromDecBefore = new long[NUMOFITERATE];
		for(int i = 0;i<NUMOFITERATE;i++){
			byte[] testKey = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(i).array();
			Nex1.printBytesHexFormat(testKey);

			byte[] meddleCipherFromEnc = nex1.encrypt(plaintext, testKey);
			byte[] meddleCipherFromDec = nex1.decrypt(knownCiphertext, testKey);
			
			BigInteger bmeddleCipherFromEnc = new BigInteger(meddleCipherFromEnc);
			BigInteger bmeddleCipherFromDec = new BigInteger(meddleCipherFromDec);
			
			lmeddleCipherFromEnc[i] = bmeddleCipherFromEnc.longValue();
			lmeddleCipherFromDec[i] = bmeddleCipherFromDec.longValue();
			lmeddleCipherFromDecBefore[i] = lmeddleCipherFromDec[i];
		}
		System.out.println("Finished first section");
		
		Arrays.sort(lmeddleCipherFromDec);
		int k1 = 0;
		int k2 = -1;
		for(k1=0;k1<NUMOFITERATE;k1++){
			System.out.println("try K1 = " + k1);
			k2 = Arrays.binarySearch(lmeddleCipherFromDec, lmeddleCipherFromEnc[k1]);
			if(k2 > -1) break;
		}

		System.out.println("----- k1 founded ------");
		System.out.println("k1 = " + k1);

		k2 = 0;
		for(k2 = 0;k2<NUMOFITERATE;k2++){
			if(lmeddleCipherFromDecBefore[k2] == lmeddleCipherFromEnc[k1]){
				break;
			}
		}
		System.out.println("----- k2 founded ------");
		System.out.println("k2 = " + k2);
		
		byte[] bk1= ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(k1).array();
		byte[] bk2 = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(k2).array();
		
		System.out.print("K1 byte form = ");
		Nex1.printBytesHexFormat(bk1);
		System.out.print("K2 byte form = ");
		Nex1.printBytesHexFormat(bk2);
	}
}
