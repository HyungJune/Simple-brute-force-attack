import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Problem1 {
	private final static int KEYSIZEBIT = 32; // 4 byte
	private final static int KEYSIZEBYTE = KEYSIZEBIT/8;
	private final static int BLOCKSIZEBIT = 48; //6 byte
	private final static int BLOCKSIZEBYTE = BLOCKSIZEBIT/8;
	
	public static void main(String args[]){
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
		knownCiphertext[0] = (byte)0xfb;
		knownCiphertext[1] = (byte)0xd0;
		knownCiphertext[2] = (byte)0x40;
		knownCiphertext[3] = (byte)0xd6;
		knownCiphertext[4] = (byte)0xdb;
		knownCiphertext[5] = (byte)0x9c;
		
	
		int intKey = 0;
		byte[] testKey = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(intKey).array();
		
		byte[] ciphertext = nex1.encrypt(plaintext, testKey);	
		//Nex1.printBytesHexFormat(testKey);
		while(true){
			Nex1.printBytesHexFormat(testKey);
			int i = 0;
			for(i = 0;i<6;i++){
				if(ciphertext[i] != knownCiphertext[i]){
					break;
				}
			}
			if(i == 6){
				break;
			}
			
			intKey--;
			testKey = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).putInt(intKey).array();
			ciphertext = nex1.encrypt(plaintext, testKey);
			
		}
		System.out.println("Sol = ");
		Nex1.printBytesHexFormat(testKey);
		System.out.println("Finished");
	}
}