# Simple-brute-force-attack

I implement a simple block encryption scheme called Nex1.

Using this encryption algorithm, I can test a pair of a plaintext and ciphertext. (in testVector)

Then, I try to find a master key used in generating a ciphertext by using brute-force attack with the pair of the ciphertext and underlying plaintext.

Also, I make a pair generted from double encryption and try to find two master keys (inner and outter) by using Meet-In-The-Middle attack.<br>
(It assumpts that the two master keys' first two bytes are 0).

#Nex1

The Nex1 algorithm in Nex1.java uses a SPN structure like AES. <br>
The SPN structure has some rounds and a round-key scheduler. <br>
The round has 3 components such as round-key XOR, confusion layer (S-box) and diffusion layer. <br>

In Nex1, the length of a master key and block in bytes is 32 and 48 respectively.

Nex1 has 10 rounds SPN.

the S-box in Nex1 is same with AES.

The linear tranform M (diffusion part) is a 3x3 MDS matrix in GF(2^8). <br>
The GF(2^8) is same with AES ( p(x) = x^8+x^4+x^3+x^1+1). <br>
The matrix M is [(1,2,2),(2,2,1),(2,1,2)] and the inverse is [(1,F7,F7),(F7,F7,1),(F7,1,F7)].

In a key schedule algorithm, It makes a key sequence W of 544 bits length from the master key K like the follow. <br>
> W = K||Rotl(K, 5)||Rotl(K, 10)||Rotl(K, 15)||Rotl(K, 20)||....||Rotl(K, 11)||Rotl(K,16). <br>
  Rotl(x,n) : returns n-bit cyclic shifted x.

Nex1 uses round keys rk0, rk1, rk2, ..., rk10, that the size of a round key is 48 bits, from left to right in W. <br>
(Nex1 uses 528 bits in W and throw away the remaining bits)


