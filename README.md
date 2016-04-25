# Simple-brute-force-attack

I implement a simple block encryption scheme called Nex1.

Using this encryption algorithm, I can test a pair of a plaintext and ciphertext. (in testVector)

Then, I try to find a master key used in generating a ciphertext by using brute-force attack with the pair of the ciphertext and underlying plaintext.

Also, I make a pair generted from double encryption and try to find two master keys (inner and outter) by using Meet-In-The-Middle attack.
(It assumpts that the two master keys' first two bytes are 0).

#Ne1



