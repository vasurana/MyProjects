package in.com.app.storage.caching.sqlight;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

  private SecretKeySpec skeySpec;
  private Cipher cipher;
  
  public Encryption(byte [] keyraw) throws Exception{
    if(keyraw == null){
      byte[] bytesOfMessage = "".getBytes("UTF-8");
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] bytes = md.digest(bytesOfMessage);
      
      skeySpec = new SecretKeySpec(bytes, "AES");
      cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }
    else{
    
    skeySpec = new SecretKeySpec(keyraw, "AES");
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    
    }
  }
  
  public Encryption(String passphrase) throws Exception{
    byte[] bytesOfMessage = passphrase.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(bytesOfMessage);
    skeySpec = new SecretKeySpec(thedigest, "AES");
    
    
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
  }
  
  public Encryption() throws Exception{
    byte[] bytesOfMessage = "".getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(bytesOfMessage);
    skeySpec = new SecretKeySpec(thedigest, "AES");
    
    skeySpec = new SecretKeySpec(new byte[16], "AES");
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
  }
  
  public byte[] encrypt (byte[] plaintext) throws Exception{
    //returns byte array encrypted with key
    
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    
    byte[] ciphertext =  cipher.doFinal(plaintext);
    
    return ciphertext;
  }
  
  public byte[] decrypt (byte[] ciphertext) throws Exception{
    //returns byte array decrypted with key
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    
    byte[] plaintext = cipher.doFinal(ciphertext);
    
    return plaintext;
  }
  
  public static void main(String[] args) throws Exception {

    String message="This is just an example";
    Encryption encrypter = new Encryption(new byte[16]);
    
    byte[] encrypted = encrypter.encrypt(message.getBytes("UTF-8"));
    byte[] decrypted = encrypter.decrypt(encrypted);
    if(decrypted!=null)
    message = new String(decrypted,"UTF-8");
//    System.out.println(new String (decrypted, "UTF-8"));

  }
}
