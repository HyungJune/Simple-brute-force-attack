public class testVector {
	private final static int KEYSIZEBIT = 32; // 4 byte
	private final static int KEYSIZEBYTE = KEYSIZEBIT/8;
	private final static int BLOCKSIZEBIT = 48; //6 byte
	private final static int BLOCKSIZEBYTE = BLOCKSIZEBIT/8;

	public static void main(String[] args){
		Nex1 nex1 = new Nex1();
		byte[] plaintext = new byte[BLOCKSIZEBYTE];
		byte[] masterKey = new byte[KEYSIZEBYTE];
		
		/** MAKE PLAINTEXT  **/
		plaintext[0] = 0x00;
		plaintext[1] = 0x11;
		plaintext[2] = 0x22;
		plaintext[3] = 0x33;
		plaintext[4] = 0x44;
		plaintext[5] = 0x55;
		
		/** MAKE MASTER KEY **/
		masterKey[0] = 0x12;
		masterKey[1] = 0x34;
		masterKey[2] = 0x56;
		masterKey[3] = 0x78;
		
		/** afterExt RELIABLILTY IN BYTE ARRAY **/
		System.out.println("plaintext length = " + plaintext.length);
		System.out.println("BLOCKSIZE IN BYTE = " + BLOCKSIZEBYTE);
		
		System.out.println("master key length = " + masterKey.length);
		System.out.println("KEYSIZE IN BYTE = " + KEYSIZEBYTE);
		
		/** Test Vector **/
		/* Encryption */
		byte[] ciphertext = nex1.encrypt(plaintext, masterKey);
		System.out.print("Ciphertext = ");
		Nex1.printBytesHexFormat(ciphertext);
		
		/* Decryption */
		byte[] decryptedCipher = nex1.decrypt(ciphertext, masterKey);
		System.out.print("decryptedCipher = ");
		Nex1.printBytesHexFormat(decryptedCipher);
	}
}