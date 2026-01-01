package com.v.addin.ssl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.os.Environment;

import android.util.Base64;
import android.util.Log;

import com.v.addin.ssl.CustomTrustManager;

public class SslUtilities {

	public SslUtilities() {
	}

	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	public void ClearSSLInfo()
	{
		KeyStore store;
		try {
			store = KeyStore.getInstance("PKCS12");
			Enumeration<String> e = store.aliases();
			for (; e.hasMoreElements();) {
				String alias = e.nextElement();
				if (store.isKeyEntry(alias)) {
					Log.d("Kimba1","ALias = " + alias);
					store.deleteEntry(alias);
				}
			}
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			Log.d("Kimba1","error msg: " + e1.getMessage());
			String m = "";
			String mm = m;
				
		}

	}
	/**
	 * Creates an SSLContext with the client and server certificates
	 * 
	 * @param clientCertFile
	 *            A File containing the client certificate
	 * @param clientCertPassword
	 *            Password for the client certificate
	 * @param caCertString
	 *            A String containing the server certificate
	 * @return An initialized SSLContext
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static SSLContext makeContext(File clientCertFile,
					String clientCertPassword) throws Exception {
			final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile,
					clientCertPassword);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
			kmf.init(keyStore, clientCertPassword.toCharArray());
			KeyManager[] keyManagers = kmf.getKeyManagers();

			TrustManager[] trustManagers = { new CustomTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.setDefault(sslContext);
			sslContext.init(keyManagers, trustManagers,null);
			return sslContext;
	}
	/**
	 * Creates an SSLContext with the client and server certificates
	 * 
	 * @param clientCertFile
	 *            A File containing the client certificate
	 * @param clientCertPassword
	 *            Password for the client certificate
	 * @param caCertString
	 *            A String containing the server certificate
	 * @return An initialized SSLContext
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static SSLContext makeContext(File clientCertFile,
					String clientCertPassword, String caCertString) throws Exception {
			final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile,
					clientCertPassword);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
			kmf.init(keyStore, clientCertPassword.toCharArray());
			KeyManager[] keyManagers = kmf.getKeyManagers();

			final KeyStore trustStore = loadPEMTrustStore(caCertString);
			TrustManager[] trustManagers = { new CustomTrustManager(trustStore) };
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.setDefault(sslContext);
			sslContext.init(keyManagers, trustManagers,null);
			return sslContext;
	}
	/**
	 * Creates an SSLContext with the client and server certificates
	 * 
	 * @param clientCertFile
	 *            A File containing the client certificate
	 * @param clientCertPassword
	 *            Password for the client certificate
	 * @param caCertString
	 *            A String containing the server certificate
	 * @return An initialized SSLContext
	 * @throws Exception
	 */
	public static SSLContext makeContext(File clientCertFile,
					String clientCertPassword, String caCertString, String caCertRaString) throws Exception {
			final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile,
					clientCertPassword);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
			kmf.init(keyStore, clientCertPassword.toCharArray());
			KeyManager[] keyManagers = kmf.getKeyManagers();

			final KeyStore trustStore = loadPEMTrustStore(caCertString);
			final KeyStore trustStoreRa = loadPEMTrustStore(caCertRaString);
			TrustManager[] trustManagers = { new CustomTrustManager(trustStore), new CustomTrustManager(trustStoreRa) };
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keyManagers, trustManagers,null);
			return sslContext;
	}

	/**
	 * Creates an SSLContext with the client and server certificates
	 * 
	 * @param clientCertFile
	 *            A File containing the client certificate
	 * @param clientCertPassword
	 *            Password for the client certificate
	 * @param caCertString
	 *            A String containing the server certificate
	 * @return An initialized SSLContext
	 * @throws Exception
	 */
	public static SSLContext makeContext(String clientCertString,
			String clientCertPassword, String caCertString) throws Exception {
		final KeyStore keyStore = loadPEMTrustStore(clientCertString);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
		kmf.init(keyStore, clientCertPassword.toCharArray());
		KeyManager[] keyManagers = kmf.getKeyManagers();

		final KeyStore trustStore = loadPEMTrustStore(caCertString);
		TrustManager[] trustManagers = { new CustomTrustManager(trustStore) };

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagers, trustManagers, null);

		return sslContext;
	}

	/**
	 * Produces a KeyStore from a String containing a PEM certificate
	 * (typically, the server's CA certificate)
	 * 
	 * @param certificateString
	 *            A String containing the PEM-encoded certificate
	 * @return a KeyStore (to be used as a trust store) that contains the
	 *         certificate
	 * @throws Exception
	 */
	private static KeyStore loadPEMTrustStore(String certificateString)
			throws Exception {

		byte[] der = loadPemCertificate(new ByteArrayInputStream(
				certificateString.getBytes()));
		ByteArrayInputStream derInputStream = new ByteArrayInputStream(der);
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance("X.509");
		X509Certificate cert = (X509Certificate) certificateFactory
				.generateCertificate(derInputStream);
		String alias = cert.getSubjectX500Principal().getName();

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null);
		trustStore.setCertificateEntry(alias, cert);

		return trustStore;
	}

	/**
	 * Produces a KeyStore from a PKCS12 (.p12) certificate file, typically the
	 * client certificate
	 * 
	 * @param certificateFile
	 *            A file containing the client certificate
	 * @param clientCertPassword
	 *            Password for the certificate
	 * @return A KeyStore containing the certificate from the certificateFile
	 * @throws Exception
	 */
	public static KeyStore loadPKCS12KeyStore(File certificateFile,
			String clientCertPassword) throws Exception {
		KeyStore keyStore = null;
		FileInputStream fis = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			fis = new FileInputStream(certificateFile);
			keyStore.load(fis, clientCertPassword.toCharArray());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ex) {
				String m = ex.getMessage();
				String mm = m;
				// ignore
			}
		}
		return keyStore;
	}

	/**
	 * Reads and decodes a base-64 encoded DER certificate (a .pem certificate),
	 * typically the server's CA cert.
	 * 
	 * @param certificateStream
	 *            an InputStream from which to read the cert
	 * @return a byte[] containing the decoded certificate
	 * @throws IOException
	 */
	private static byte[] loadPemCertificate(InputStream certificateStream)
			throws IOException {

		byte[] der = null;
		BufferedReader br = null;

		try {
			StringBuilder buf = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(certificateStream));

			String line = br.readLine();
			while (line != null) {
				if (!line.startsWith("--")) {
					buf.append(line);
				}
				line = br.readLine();
			}

			String pem = buf.toString();
			der = Base64.decode(pem, Base64.DEFAULT);

		} finally {
			if (br != null) {
				br.close();
			}
		}

		return der;
	}

	public static File getCertFile(String certificateName) {
		if (certificateName != null)
		{
			File externalStorageDir = Environment.getExternalStorageDirectory();
			return new File(externalStorageDir, certificateName);
		}
		return null;
	}

	public static String readCert(String certificateName) throws Exception {
		File externalStorageDir = Environment.getExternalStorageDirectory();
		File caCertFile = new File(externalStorageDir, certificateName);
		InputStream inputStream = new FileInputStream(caCertFile);
		return readFully(inputStream);
	}

	public static String readFully(InputStream inputStream) throws IOException {

		if (inputStream == null) {
			return "";
		}

		BufferedInputStream bufferedInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;

		try {
			bufferedInputStream = new BufferedInputStream(inputStream);
			byteArrayOutputStream = new ByteArrayOutputStream();

			final byte[] buffer = new byte[2048];
			int available = 0;

			while ((available = bufferedInputStream.read(buffer)) >= 0) {
				byteArrayOutputStream.write(buffer, 0, available);
			}

			return byteArrayOutputStream.toString();

		} finally {
			if (bufferedInputStream != null) {
				bufferedInputStream.close();
			}
		}
	}

}
