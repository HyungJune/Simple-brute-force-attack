import java.nio.ByteBuffer;

public class VerifyProblem2 {
	private final static int KEYSIZEBIT = 32; // 4 byte
	private final static int KEYSIZEBYTE = KEYSIZEBIT/8;
	private final static int BLOCKSIZEBIT = 48; //6 byte
	private final static int BLOCKSIZEBYTE = BLOCKSIZEBIT/8;
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
		
		/** MAKE KNOWN CIPHERTEXT **/
		
		knownCiphertext[0] = (byte)0xb2;
		knownCiphertext[1] = (byte)0x31;
		knownCiphertext[2] = (byte)0xbb;
		knownCiphertext[3] = (byte)0x5a;
		knownCiphertext[4] = (byte)0x65;
		knownCiphertext[5] = (byte)0x8b;
		
		
		int k1 = 10863504;
		int k2 = 1090296;
		
		byte[] bk1= ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(k1).array();
		byte[] bk2 = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(k2).array();
		byte[] mCipher = nex1.encrypt(plaintext, bk1);
		byte[] cipher = nex1.encrypt(mCipher, bk2);
		System.out.print("Problem 2 first key : " );
		Nex1.printBytesHexFormat(bk1);
		System.out.print("Problem 2 second key : ");
		Nex1.printBytesHexFormat(bk2);
		
		System.out.print("Problem 2 Known cipher : ");
		Nex1.printBytesHexFormat(knownCiphertext);
		
		System.out.print("Problem 2 Generated Cipher : ");
		Nex1.printBytesHexFormat(cipher);
	}

}
