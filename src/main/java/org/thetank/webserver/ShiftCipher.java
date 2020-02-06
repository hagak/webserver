package org.thetank.webserver;

import java.io.IOException;

public class ShiftCipher {

    private int shift = 0;
    private String clearText;

	public ShiftCipher(int shift, String clearText) {
        this.shift = shift;
        this.clearText = clearText;
	}

    /**
	 * Note this shiftCipher only works on a 26 character charset. Specifically
	 * [a-z].
	 * @param clearText text to be shifted
	 * @return cipherText shifted text
	 * @throws IOException
	 * @throws CipherException if the charset is in the cleartext is invalid
	 */
	public String execute() throws IOException, CipherException {
		StringBuilder sb = new StringBuilder();
		
		char[] chrArray = this.clearText.toLowerCase().toCharArray();
		for(char ch : chrArray){
			//If the character is a SPACE then do not shift
			if(ch == ' '){
				sb.append(ch);
				continue;
			}
			//Check that character is within [a-z]
			if(ch < 'a' || ch > 'z'){
				//Throw exception
				throw new CipherException("invalid charset in message");
			}
			int offset = (int)'a';
			char out = (char) (((((int)ch-offset) + shift) % 26) + offset);
			sb.append(out);
		}
		return sb.toString();
	}
}
