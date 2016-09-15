package systems;

import java.util.*;

import utils.RanPrime;

/**
 * Builds a RSA system generating 
 * random prime number p, q, and n, public key e, and private key d.
 * @author kc
 */
public class RsaSys {
	private static final int SIZE = 32;
	
	private String name;
	private int p, q, n, e, d;
	
	private boolean traceDone = false;
	private final ArrayList<String> trace = new ArrayList<>();
	private String tmp = "";
	
	public RsaSys() {
		this("");
	}
	
 	public RsaSys(final String name) {
 		this.name = name;
 		
 		do {
			RanPrime rp = new RanPrime();
			p = rp.getP();
			copyTrace(rp);
			
			do {
				rp = new RanPrime();
				q = rp.getP();
			} while (q == p);
			
			if(rp.haveTrace()) {
				trace.set(1, rp.getTrace()[1]);
				traceDone = true;
			}
			
			if(!traceDone) {
				trace.set(1, trace122());
			}
			
			n = p * q;
			e = genE((p-1) * (q-1));
 		} while(e == 0);
 		
		d = inverse(e, (p-1)*(q-1));
		trace.add("line 155\nd = " + d + "\n\n");
		
 		trace.add("line 159\nInteger:\n" + 
 				String.format("p=%d, q=%d, n=%d, e=%d, d=%d\n", p, q, n, e, d) +
 				"Bits:\n" + 
 				String.format("p=%s\nq=%s\nn=%s\ne=%s\nd=%s\n",
 						getBinary(p), getBinary(q), getBinary(n),
 						getBinary(e), getBinary(d)));
	}
 	
	private void copyTrace(final RanPrime rp) {
		if(!traceDone) {
			if(trace.isEmpty()) {
				for(String s: rp.getTrace()) {
					trace.add(s);
				}
			} else {
				for(int i = 0; i <= 2; i++) {
					trace.set(i, rp.getTrace()[i]);
				}
			}
			
			if(rp.haveTrace()) {
				traceDone = true;
			}
		}
	}
	
 	private String trace122() {
 		final RanPrime rp = new RanPrime();
 		int n = 95;
 		while(rp.isPrime(n))
 			n *= 2;
 		
 		return rp.getTrace()[1];
 	}
 	
 	private int genE(int phiN) {
 		int x = 0;
 		int i = 0;
 		for (i = 3; i <= phiN && !isRelPrime(phiN, i); i++);
 		x = i;
 		
 		trace.add("line 145\n" + tmp + "\n");
 		
 		return x;
 	}

 	private boolean isRelPrime(int a, int b) {
		tmp += "try e = " + b + "\n";

		int i = 0, ri, ri1 = a, ri2 = b;
		int qi, qi2 = 0, qi1 = 0;
		int si, si2 = 1, si1 = 0;
		int ti, ti2 = 0, ti1 = 1;
		do {
			i++;
			ri = ri1;
			ri1 = ri2;
			qi = ri/ri1;
			ri2 = ri%ri1;
			if (i == 1) {
				si = 1;
				ti = 0;
				qi2 = qi;
			} else if (i == 2) {
				si = 0;
				ti = 1;
				qi1 = qi;
			} else {
				si = si2 - qi2*si1;
				si2 = si1;
				si1 = si;
				ti = ti2 - qi2*ti1;
				ti2 = ti1;
				ti1 = ti;
				qi2 = qi1;
				qi1 = qi;
			}
			
			tmp += String.format("i=%d, qi=%d, ri=%d, r(i+1)=%d, r(r+2)=%d, si=%d, ti=%d\n", 
							i, qi, ri, ri1, ri2, si, ti);
		} while(ri2 > 0);
		
		i++;
		ri = ri1;
		si = si2 - qi2*si1;
		ti = ti2 - qi2*ti1;
		
		tmp += String.format("i=%d, ri=%d, si=%d, ti=%d\n", 
						i, ri, si, ti);
		
		return ri1 == 1;
	}

 	private int inverse(int e, int phiN) {
 		
 		int t = 0;
 		int r = phiN;
 		int newt = 1;
 		int newr = e;
 		int q = 0;
 		int x = 0;
 	
 		while (newr != 0) {
 			q = r/newr;
 			x = t;
 			t = newt;
 			newt = x - q * newt;
 			x = r;
 			r = newr;
 			newr = x - q * newr;
 			
 			if (t < 0) 
 				t += phiN;
 		}
 		
 		return t;
 	}
 	
 	/**
 	 * Generates a random number of n bits, and each bit is random.
 	 * @param bits number of bits
 	 * @return
 	 */
	public int genBits(int bits) {
		int x = 0;
		int b = 0;
		final Random r = new Random();
		
		for (int i = 0; i < bits; i++) {
			b = Math.abs(r.nextInt()) % 2;
			x += b << i;
		}
		
		return x;
	}
	
	/**
   * Converts decimal to a 4-byte binary
   * @param x original decimal
   * @return 4-byte binary
   */
  public String getBinary(int x) {
    final StringBuilder sb = new StringBuilder();
    sb.append(Integer.toBinaryString(x));
    int len = sb.length();
    
    for (int i = 0; i <= SIZE - len; i++) {
      sb.insert(0, "0");
    }
    
    return sb.toString();
  }
	
	public String getTrace() {
		String t = "";
		for(String s : trace)
			t += s;
		
		return t;
	}
	
	public int getN() {
		return n;
	}
	
	public int getPubK() {
		return e;
	}
	
	public int getPrtK() {
		return d;
	}
	
	public void setName(final String nm) {
		name = nm;
	}
	
	public String getName() {
		return name;
	}
	
	public void printTrace(int i) {
		System.out.println(trace.get(i));
	}
}