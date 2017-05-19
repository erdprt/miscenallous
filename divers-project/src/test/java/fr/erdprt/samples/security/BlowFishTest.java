package fr.erdprt.samples.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlowFishTest {

	private final static Logger logger = LoggerFactory.getLogger(BlowFishTest.class);

	static final String KEY_FILE_PROPERTY = "spring.path";

	static final String KEY_DIR_PROPERTY = "./keys";

	static String charset = "UTF-8";

	static final String ALGO = "Blowfish";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String keyFile = new File(KEY_DIR_PROPERTY, "crc.txt").getAbsolutePath();
		System.setProperty(KEY_FILE_PROPERTY, keyFile);

		BlowFishTest instance = new BlowFishTest();

		instance.generateFileKey();

		instance.run("admin,admin123*", "admin.txt");

	}

	public void run(String secret, String fileName) throws Exception {

		generateFileKey();

		String encoded = encode(secret);
		logger.info("encoded={}", encoded);
		generateEncodedFileKey(encoded, fileName);

		String decoded = decode(encoded);

		logger.info("decoded={}", decoded);

	}

	public static byte[] getKey() throws IOException {
		logger.debug("Decryptage du résultat à partir de la clé contenue dans le fichier {}", System.getProperty(KEY_FILE_PROPERTY));
		FileInputStream fis = new FileInputStream(System.getProperty(KEY_FILE_PROPERTY));
		byte[] b = new byte[100];
		int n = fis.read(b);
		fis.close();
		byte[] key = new byte[n];
		System.arraycopy(b, 0, key, 0, n);
		return key;
	}

	public String encode(final String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IOException {
		byte[] kbytes = getKey();
		SecretKeySpec key = new SecretKeySpec(kbytes, ALGO);
		Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encoding = cipher.doFinal(secret.getBytes());
		BigInteger n = new BigInteger(encoding);
		return n.toString(16);
	}

	public String decode(final String code) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IOException {
		logger.debug("Obtention du résultat à partir de la valeur codée {}", code);
		byte[] kbytes = getKey();
		SecretKeySpec key = new SecretKeySpec(kbytes, ALGO);
		BigInteger n = new BigInteger(code, 16);
		byte[] encoding = n.toByteArray();
		// SECURITY-344: fix leading zeros
		if (encoding.length % 8 != 0) {
			int length = encoding.length;
			int newLength = (length / 8 + 1) * 8;
			int pad = newLength - length; // number of leading zeros
			byte[] old = encoding;
			encoding = new byte[newLength];
			for (int i = old.length - 1; i >= 0; i--) {
				encoding[i + pad] = old[i];
			}
			// SECURITY-563: handle negative numbers
			if (n.signum() == -1) {
				for (int i = 0; i < newLength - length; i++) {
					encoding[i] = (byte) -1;
				}
			}
		}
		Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decode = cipher.doFinal(encoding);

		return new String(decode);
	}

	public void generateFileKey() throws Exception {
		byte[] key = generateKey();
		FileOutputStream outputStream = new FileOutputStream(System.getProperty(KEY_FILE_PROPERTY));
		outputStream.write(key);
		outputStream.close();
	}

	public void generateEncodedFileKey(String encoded, String fileName) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(new File(KEY_DIR_PROPERTY, fileName).getAbsolutePath());
		outputStream.write(encoded.getBytes());
		outputStream.close();
	}

	public byte[] generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(ALGO);
		keyGen.init(128);
		return keyGen.generateKey().getEncoded();
	}
}
