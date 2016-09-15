package systems;

/**
 * Builds a Cryptography system that allows
 * digital certification generation client and identity authentication for for a RSA system.
 * @author kc
 */
public class CryptoSystem {
	private static final int BYTE = 8;
	private static final int NAMEBYTE = 6 * BYTE;
	private static final int NBYTE = 4 * BYTE;
	private static final int KEYBYTE = 4 * BYTE;
	private static final String[] TRACE = new String[4];
	
	/**
	 * Generates certificate for two clients
	 * @param a normal client
	 * @param t trent with authority
	 * @return
	 */
	public String[] genCertificate(final RsaSys a, final RsaSys t) {
		final String r = genR(a, t);
		final int hr = genHash(r);
		final int s = fastExponentiation(hr, t.getPrtK(), a.getN(), false);
		final String[] c = {r, getBinary(hr, 32), getBinary(s, 32)};
		
		return c;
	}
	
	private String genR(final RsaSys a, final RsaSys t) {
	  final StringBuilder sb = new StringBuilder();
		
		sb.append(getBiName(a.getName()));
		int RLen = sb.length();
		for (int i = 0; i < NAMEBYTE - RLen; i++) {
		  sb.insert(0, "0");
		}
		sb.append(getBinary(a.getN(), NBYTE));
		sb.append(getBinary(a.getPubK(), KEYBYTE));
		
		return sb.toString();
	}
	
	private String getBiName(final String s) {
		final char[] bn = s.toCharArray();
		final StringBuilder sb = new StringBuilder();
		for(char c : bn) {
		  sb.insert(0, getBinary((int)c, BYTE));
		}
		
		return sb.toString();
	}
	
	private String getBinary(int x, int size) {
	  final StringBuilder sb = new StringBuilder();
	  sb.append(Integer.toBinaryString(x));
	  int len = sb.length();
		for (int i = 1; i <= size - len; i++) {
		  sb.insert(0, "0");
		}
		
		return sb.toString();
	}
	
	private int genHash(final String r) {
		final char[] c = r.substring(0, 8).toCharArray();
		
		// partition r into bytes, and compute exclusive or
		for (int i = BYTE; i <= r.length() - BYTE; i += BYTE) {
			final char[] m = r.substring(i, i+BYTE).toCharArray();
			for (int j = 0; j < BYTE; j++) {
			  c[j] = xor(m[j], c[j]);
			}
		}
		
		return Integer.parseInt(new String(c),2);
	}
	
	private char xor (char m, char c) {
		if (m == c) {
			return '0';
		}
		
		return '1';
	}
	
	private int fastExponentiation(int a, int x, int n, boolean p) {
		String t = "";
		final char[] c = Integer.toBinaryString(x).toCharArray();
		int y = 1;
		int k = c.length - 1;

		for (int i = k; i >= 0; i--) {
			y = (y * y) % n;
			t += String.format("i = %d, x = %s, y1 = %d, ", i, c[k-i], y);

			if (c[k-i] == '1') 
				y = (a * y) % n;
			t += "y2 = " + y + "\n";
		}
		
		t = String.format("Trace: n = %d, a = %d, x = %d\n", n, a, x) + t;
		if(p){
		  TRACE[3] = "line 226\n" + t;
		}
		
		return y;
	}
	
	/**
	 * Stores the trace for creating a digital certificate
	 * @param c String array of certificate token traces
	 * @return certificate in string
	 */
	public String getCertiTrace(String[] c) {
		String s = String.format("line 188\nr = %s\nh(r) = %s\ns = %s\n\n", c[0], c[1], c[2]);
		s += String.format("line 190\nh(r) = %d\ns = %d\n", Integer.parseInt(c[1], 2), Integer.parseInt(c[2], 2));
		
		return s;
	}
	
	/**
	 * Authenticates A to B
	 * @param a RSA client needs authentication
	 * @param b RSA client needs the other's authentication
	 * @return true if A authenticates itself
	 */
	public boolean authentication(final RsaSys a, final RsaSys b) {
		int u = genU(a, b);
		int hu = genHash(getBinary(u, 32));
		int v = fastExponentiation(hu, a.getPrtK(), a.getN(), false);
		int w = fastExponentiation(v, a.getPubK(), a.getN(), true);
		
		final String s = String.format("u = %d (%s)\n"
		    + "h(u) = %d (%s)\n"
		    + "v = D(d, h(u)) = %d (%s)\n"
		    + "E(e, v) = %d (%s)",
				u, getBinary(u, 32), hu, getBinary(hu, 32), v, getBinary(v, 32), w, getBinary(w, 32));
		TRACE[2] = "line 222\n" + s;
		
		return w == hu;
	}
	
	private int genU(final RsaSys a, final RsaSys b) {
		int n = a.getN();
		int k = 0;
		while (n >> k != 1) {
			k++;
		}
		int u = 1 << (k-1);
		
		int nbit = (k-1) / 2;
		int v = a.genBits(k - 1 - nbit) << nbit;
		int w = b.genBits(nbit);
		u += v + w;
		
		TRACE[0] = "line 214\nk = " + k + ", u = " + u; 
		TRACE[1] = "line 216\nu = " + getBinary(u, 32);
		
		return u;
	}
	
	public String getTrace() {
	  final StringBuilder sb = new StringBuilder();
	  for (String s : TRACE) {
	    sb.append(s);
	    sb.append("\n\n");
	  }

		return sb.toString();
	}
}
