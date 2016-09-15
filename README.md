# RSA-Cryptosystem
This is a project for course Applied Cryptography.

Assignment: RSA Public/Private Cryptosystem Project
Course: Applied Cryptography and Network Security
Description:
1. RSA system
    a. generate random primes p, q, get n
    b. find public key e relatively prime to phi(n)
    c. find private key d multiplicatively inversed to e
2. Digital certification
    a. generate the public key pair <name, <n, e>>
    b. sign the h(pair)
3. Authentication (A to B)
    a. cooperatively generate a random number u
    b. A decrypts h(u) with A.d, gets v and sends it to B
    c. B encrypts v with A.e, and is supposed to get h(u)
