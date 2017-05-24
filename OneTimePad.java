package Chat_Project_Final;

import java.util.Random;

public class OneTimePad {

 private String plainMessage = "";
 private String encryptedMessage = "";
 private String currentKey = "";
 
 public OneTimePad() {
  
  plainMessage = "no message";
  currentKey = generateKey(plainMessage);
  encryptedMessage = encrypt(plainMessage);
  

 }
 public OneTimePad(String msg) {
  
  plainMessage = msg;
  currentKey = generateKey(msg);
  encryptedMessage = encrypt(msg);
 }
 
 private int getNumFromChar(char c) {
  return Character.valueOf(c);
 }
 
 private char getCharFromNum(int num){
  return Character.toChars(num)[0];
 }
 
 public String encrypt(String plainMessage){
  String encMsg = "";
  for (int i = 0; i <plainMessage.length();i++){
   int numForPlainChar = getNumFromChar(plainMessage.charAt(i)); // get num from plain char
   int numForKeyChar = getNumFromChar(currentKey.charAt(i)); // get num from the key's char
   int numForEncChar = numForPlainChar + numForKeyChar;
   char encryptedChar = getCharFromNum(numForEncChar);
   encMsg += encryptedChar; // append the char to our encrypted message

  }  
  return encMsg;
 }
 
 public String decrypt(String encMsg){
  String decMsg = "";
  for(int i = 0; i <encMsg.length(); i++){
   int numForEncChar = getNumFromChar(encMsg.charAt(i)); //encMsg
   int numForKeyChar = getNumFromChar(currentKey.charAt(i));
   int numForPlainChar = numForEncChar - numForKeyChar;
   char plainChar = getCharFromNum(numForPlainChar);
   decMsg += plainChar;
  }
  return decMsg;
 }
 
 public String generateKey(String plainMsg){
   
  String generatedKey = "";
  Random r = new Random(100);
  for (int i = 0; i<100; i++){
   int randNum = 64 + (int)(r.nextDouble()*26);
   generatedKey+= getCharFromNum(randNum);
  }
  return generatedKey;
 }
 
 public static void main(String [] args){
  OneTimePad otp = new OneTimePad("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
  //OneTimePad otp = new OneTimePad("@: abcdefghijklmnopqrstuvwxyz");
  
  System.out.println("The Plain Message: " + otp.plainMessage);
  System.out.println("The Generated Key for the Message: " + otp.currentKey);
  String encMsg = otp.encryptedMessage;
  System.out.println("The Encrypted Message: " + encMsg);
  
  System.out.println("The Plain Message: "+ otp.decrypt(encMsg));
 }


}
