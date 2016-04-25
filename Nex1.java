import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Nex1 {
	private final static int KEYSIZEBIT = 32; // 4 byte
	private final static int KEYSIZEBYTE = KEYSIZEBIT/8;
	private final static int BLOCKSIZEBIT = 48; //6 byte
	private final static int BLOCKSIZEBYTE = BLOCKSIZEBIT/8;
	private final static int ROUND = 10;
	private static final char sbox[] = { 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe,
			0xd7, 0xab, 0x76, 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72,
			0xc0, 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, 0x04,
			0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c,
			0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, 0x53, 0xd1, 0x00, 0xed, 0x20,
			0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33,
			0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc,
			0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e,
			0x3d, 0x64, 0x5d, 0x19, 0x73, 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde,
			0x5e, 0x0b, 0xdb, 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4,
			0x79, 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, 0xba,
			0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e, 0xb5,
			0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, 0xe1, 0xf8, 0x98, 0x11, 0x69,
			0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42,
			0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 };
	
	private static final char rsbox[] = { 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81,
			0xf3, 0xd7, 0xfb, 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9,
			0xcb, 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e, 0x08,
			0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25, 0x72, 0xf8, 0xf6,
			0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92, 0x6c, 0x70, 0x48, 0x50, 0xfd,
			0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84, 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3,
			0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06, 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1,
			0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b, 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf,
			0xce, 0xf0, 0xb4, 0xe6, 0x73, 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c,
			0x75, 0xdf, 0x6e, 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe,
			0x1b, 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4, 0x1f,
			0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f, 0x60, 0x51, 0x7f,
			0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef, 0xa0, 0xe0, 0x3b, 0x4d, 0xae,
			0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61, 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6,
			0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d };
	
	private static byte[][] Table0 = new byte[256][3];

	private static byte[][] Table1 = new byte[256][3];

	private static byte[][] Table2 = new byte[256][3];
	private static byte[] T0 = new byte[3];
	private static byte[] T1 = new byte[3];
	private static byte[] T2 = new byte[3];
	
	private static byte[][] rTable0 = new byte[256][3];
	private static byte[][] rTable1 = new byte[256][3];
	private static byte[][] rTable2 = new byte[256][3];
	private static byte[] rT0 = new byte[3];
	private static byte[] rT1 = new byte[3];
	private static byte[] rT2 = new byte[3];
	
	public Nex1(){
		makeTable();
	}
	
	private static void makeTable(){
		for(int i = 0;i<256;i++){
			Table0[i][0] = GFMult((byte) 01, (byte) i);
			Table0[i][1] = GFMult((byte) 02, (byte) i);
			Table0[i][2] = GFMult((byte) 02, (byte) i);
			
			Table1[i][0] = GFMult((byte) 02, (byte) i);
			Table1[i][1] = GFMult((byte) 02, (byte) i);
			Table1[i][2] = GFMult((byte) 01, (byte) i);
			
			Table2[i][0] = GFMult((byte) 02, (byte) i);
			Table2[i][1] = GFMult((byte) 01, (byte) i);
			Table2[i][2] = GFMult((byte) 02, (byte) i);
	
			rTable0[i][0] = GFMult((byte) 01, (byte) i);//
			rTable0[i][1] = GFMult((byte) 0xf7, (byte) i);
			rTable0[i][2] = GFMult((byte) 0xf7, (byte) i);
			
			rTable1[i][0] = GFMult((byte) 0xf7, (byte) i);
			rTable1[i][1] = GFMult((byte) 0xf7, (byte) i);
			rTable1[i][2] = GFMult((byte) 01, (byte) i);//
			
			rTable2[i][0] = GFMult((byte) 0xf7, (byte) i);
			rTable2[i][1] = GFMult((byte) 01, (byte) i);//
			rTable2[i][2] = GFMult((byte) 0xf7, (byte) i);
		}
		
	}

	public static byte[] encrypt(byte[] plaintext, byte[] masterKey){
		
		/** key schedule **/
		byte[] w = keyScheduler(masterKey);
		byte[][] roundKeyList = new byte[ROUND+1][6];
		for(int i = 0;i<ROUND+1;i++){
			int start = i * 6;
			byte[] temp = Arrays.copyOfRange(w, start, 6 + start);
			roundKeyList[i] = temp;
		}
		/*for(int i = 0;i<roundKeyList.size();i++){
			System.out.print(i + "th Round Key : ");
			printBytesHexFormat(roundKeyList.get(i));
		}*/
		/** round **/
		byte[][] output = new byte[11][plaintext.length];
		output[0] = plaintext;
		for(int i = 0;i<10;i++){
			output[i+1] = round(output[i], roundKeyList[i]);
		}
		
		/*for(int i = 0;i<output.length;i++){
			System.out.print("Output " + i + " = ");
			printBytesHexFormat(output[i]);
		}
		*/
		/** last Xor **/
		byte[] ciphertext = new byte[plaintext.length];
		for(int i = 0;i<plaintext.length;i++){
			ciphertext[i] = (byte)(output[10][i] ^ roundKeyList[10][i]);
		}
		
		return ciphertext;
	}
	public static byte[] decrypt(byte[] ciphertext, byte[] masterKey){
		/** key schedule **/
		byte[] w = keyScheduler(masterKey);
		byte[][] roundKeyList = new byte[ROUND+1][6];
		for(int i = 0;i<ROUND+1;i++){
			int start = i * 6;
			byte[] temp = Arrays.copyOfRange(w, start, 6 + start);
			roundKeyList[i] = temp;
		}
		
		/** last Xor **/
		byte[] lastXor = new byte[ciphertext.length];
		for(int i = 0;i<ciphertext.length;i++){
			lastXor[i] = (byte)(ciphertext[i] ^ roundKeyList[10][i]);
		}
		
		/** reverse round **/
		byte[][] output = new byte[11][ciphertext.length];
		output[10] = lastXor;
		for(int i = 9;i>=0;i--){
			output[i] = reverseRound(output[i+1], roundKeyList[i]);
		}
		return output[0];
	}
	private static byte[] round(byte[] input, byte[] key){
		byte[] afterSBox = new byte[input.length];
		for(int i = 0;i<input.length;i++){
			afterSBox[i] = (byte)sbox[((byte) (input[i] ^ key[i]) & 0xFF)];
		}
		//System.out.print("S Box output = ");
		//printBytesHexFormat(afterSBox);
		/* M */
		byte[] firstM = new byte[3];
		firstM[0] = afterSBox[1];
		firstM[1] = afterSBox[2];
		firstM[2] = afterSBox[3];
		byte[] resultF = transformM(firstM);
		
		byte[] secondM = new byte[3];
		secondM[0] = afterSBox[4];
		secondM[1] = afterSBox[5];
		secondM[2] = afterSBox[0];
		byte[] resultS = transformM(secondM);
		byte[] result = ByteBuffer.allocate(resultF.length + resultS.length).put(resultF).put(resultS).array();
		return result;
	}
	private static byte[] reverseRound(byte[] input, byte[] key){
		byte[] firstM = Arrays.copyOfRange(input, 0, 3);
		//printBytesHexFormat(firstM);
		byte[] secondM = Arrays.copyOfRange(input, 3, input.length);
		//printBytesHexFormat(secondM);
		byte[] resultF = reverseTransformM(firstM);
		byte[] resultS = reverseTransformM(secondM);
		
		byte[] resultM = new byte[6];
		resultM[0] = resultS[2];
		resultM[1] = resultF[0];
		resultM[2] = resultF[1];
		resultM[3] = resultF[2];
		resultM[4] = resultS[0];
		resultM[5] = resultS[1];
		
		byte[] afterReverSBox = new byte[input.length];
		byte[] result = new byte[input.length];
		for(int i = 0;i<input.length;i++){
			afterReverSBox[i] = (byte)rsbox[resultM[i] & 0xFF];
			result[i] = (byte)(afterReverSBox[i] ^ key[i]);
		}
		return result;
		
	}
	private static byte[] transformM(byte[] input){
		T0 = Table0[input[0] & 0xFF];
		T1 = Table1[input[1] & 0xFF];
		T2 = Table2[input[2] & 0xFF];
		
		byte[] result = new byte[3];
		for(int i = 0;i<3;i++){
			result[i] = (byte)(T0[i]^T1[i]^T2[i]);
		}
		return result;
	}
	
	private static byte[] reverseTransformM(byte[] input){
		rT0 = rTable0[input[0] & 0xFF];
		rT1 = rTable1[input[1] & 0xFF];
		rT2 = rTable2[input[2] & 0xFF];
		
		byte[] result = new byte[3];
		for(int i = 0;i<3;i++){
			result[i] = (byte)(rT0[i]^rT1[i]^rT2[i]);
		}
		return result;
	}
	private static byte[] keyScheduler(byte[] masterKey){
		ArrayList<byte[]> keyArrayList = new ArrayList();
		for(int i = 0; i < 340;i += 5){
			int step = i % KEYSIZEBIT;
			byte[] temp = rotateLeft(masterKey, KEYSIZEBIT, step);
			keyArrayList.add(temp);
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try{
			for(int i = 0;i<keyArrayList.size();i++){
				outputStream.write(keyArrayList.get(i));
			}

			byte[] w = outputStream.toByteArray();
			outputStream.close();

			return w;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] rotateLeft(byte[] in, int len, int step) {
		byte[] out = new byte[in.length];
		for (int i=0; i<len; i++) {
			int val = getBit(in,(i+step)%len);
			setBit(out,i,val);
		}
		return out;
	}
	
	private static byte GFMult(byte a, byte b){
		byte r = 0, t;
		while(a != 0){
			if((a & 1) != 0) r = (byte)(r ^ b);
			
			t = (byte)(b & 0x80);
			b = (byte)(b << 1);
			
			if(t != 0) b = (byte) (b ^ 0x1b);
			
			a = (byte)((a & 0xff) >> 1);
		}
		return r;
	}
	
	private static void setBit(byte[] data, int pos, int val) {
		int posByte = pos/8; 
		int posBit = pos%8;
		byte oldByte = data[posByte];
		oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
		byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
		data[posByte] = newByte;
	}
	
	private static int getBit(byte[] data, int pos) {
		int posByte = pos/8; 
		int posBit = pos%8;
		byte valByte = data[posByte];
		int valInt = valByte>>(8-(posBit+1)) & 0x0001;
	    return valInt;
	}
	
	public static void printBytesHexFormat(byte[] w){
		final StringBuilder builder = new StringBuilder();
		for(byte b : w) {
			builder.append(String.format("%02x", b));
		}
		System.out.println(builder.toString());
	}
	
	public static void printBytes(byte[] data, String name) {
		System.out.println("");
		System.out.println(name+":");
		for (int i=0; i<data.length; i++) {
			System.out.print(byteToBits(data[i])+" ");
		}
		System.out.println();
	}
	
	public static String byteToBits(byte b) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<8; i++)
			buf.append((int)(b>>(8-(i+1)) & 0x0001));
		return buf.toString();
	}
	
	
}