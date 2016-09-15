package main;

import java.io.*;

import systems.CryptoSystem;
import systems.RsaSys;

public class TestCryptoSystem {

	public static void main(String[] args) {
		
		boolean verbose = true;
		boolean printFile = false;
		
		final RsaSys Alice = new RsaSys("Alice");
		final RsaSys Trent = new RsaSys("Trent");
		System.out.println(Alice.getTrace());
		
		final CryptoSystem cp = new CryptoSystem();
		final String[] certificate_Alice = cp.genCertificate(Alice, Trent);
		System.out.println(cp.getCertiTrace(certificate_Alice));
		
		if (verbose)
			System.out.print(cp.getTrace());
		
		if (printFile) {
			try {
				final PrintWriter pw = new PrintWriter("trace.txt");
				pw.println(cp.getTrace());
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
