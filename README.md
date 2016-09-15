# RSA-Cryptosystem
This is a project for course Applied Cryptography.

Assignment: RSA Public/Private Cryptosystem Project<br />
Course: Applied Cryptography and Network Security<br />
Description:<br />
1. RSA system<br />
    a. generate random primes p, q, get n<br />
    b. find public key e relatively prime to phi(n)<br />
    c. find private key d multiplicatively inversed to e<br />
2. Digital certification<br />
    a. generate the public key pair <name, <n, e>><br />
    b. sign the h(pair)<br />
3. Authentication (A to B)<br />
    a. cooperatively generate a random number u<br />
    b. A decrypts h(u) with A.d, gets v and sends it to B<br />
    c. B encrypts v with A.e, and is supposed to get h(u)<br />
    
