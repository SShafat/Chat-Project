package Chat_Project_Final;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.Arrays;

public class ChatClientGUI extends JFrame implements ActionListener, Runnable {
 
 private Socket socket = null;
 private String serverName = "127.0.0.1"; // or "localhost" // or your friend's ip
 private final int serverPortNumber = 8080; // needs to match
 
 private DataInputStream strIn = null;
 private DataOutputStream strOut = null;
 
 private ChatClientThread client = null;
 
 private boolean done = true; //until connected you are "done"
 private String line = "";
 
 
 private JTextArea displayText = new JTextArea(10,10);
 private JTextField input = new JTextField();
 private JButton btnConnect = new JButton("Connect");
 private JButton btnSend = new JButton("Send");
 
 private JButton btnQuit = new JButton("Quit");
 private JButton btnPrivate = new JButton("Private");
 private JPanel mainJP = new JPanel(); // everything goes in here
 private JPanel displayJP = new JPanel(); //textarea
 private JPanel btnsJP = new JPanel(); // put this on the bottom
 
 
 
 public ChatClientGUI(){
	 //Title is generic since the server assigns the IDs, the client does not know it ahead of time.
	 
  this.setTitle("Chat v1.0.0");
  mainJP.setLayout(new BorderLayout(80,80));
  displayJP.setLayout(new GridLayout(2,1));
  displayJP.add(displayText); //added textarea to jpanel
  displayJP.add(input); // added input below textarea to jpanel
  btnsJP.setLayout(new GridLayout(1,4));
  
  btnPrivate.addActionListener(this);
  btnConnect.addActionListener(this);
  btnSend.addActionListener(this);
  btnQuit.addActionListener(this);
  
  btnConnect.setActionCommand("connect");
  btnQuit.setActionCommand("disconnect");
  btnPrivate.setActionCommand("private");
  btnSend.setActionCommand("send");
  
  btnPrivate.setEnabled(false);
  btnSend.setEnabled(false);
  btnConnect.setEnabled(true);
  
  btnsJP.add(btnPrivate);
  btnsJP.add(btnConnect);
  btnsJP.add(btnSend);
  btnsJP.add(btnQuit);
  
  mainJP.add(displayJP, BorderLayout.CENTER); // add to center
  
  mainJP.add(btnsJP, BorderLayout.SOUTH); // add to bottom
  
  add(mainJP);
  
  
 }
 

 @Override
 public void run() {
  while(!done){
   try {
    line = strIn.readUTF();
    /*
     * Used to detect if an encrypted string is received
     * Hash marks used to denoted that we are sending/receiving an encrypted message
     */
    String[] params = line.split("#"); 
    if (params.length == 2 && line.charAt(line.length()-1) == '#') {
      OneTimePad otp = new OneTimePad();
      line = params[0] + otp.decrypt(params[1]);
    }
    displayText.append("\n" + line);
   } catch (IOException e) {
    // TODO Auto-generated catch block
    //e.printStackTrace();
   }
   //displayMessage(line);
  }
 }



 @Override
 public void actionPerformed(ActionEvent e) {
  
  String btnClicked = e.getActionCommand();
  switch(btnClicked){
  // do something when connect 
  //connect(serverName, serverPortNumber);
  case "connect":
   Connect(serverName, serverPortNumber);
   break;
  // do something when send
  //send()
  
  case "send":
   send();
   break;
   
  // do something when private
  //send... private... 
  case "private":
   sendPrivate();
   break;
   
  // do something when quit
  //disconnect();
  case "disconnect":
   disconnect();
   break;
  }
  
  
 }
 
 private void sendPrivate() {
  String recipient = JOptionPane.showInputDialog(this, "Enter recipient ID:");
  String message = JOptionPane.showInputDialog(this, "Enter private message:");
  int encrypt = JOptionPane.showConfirmDialog(this, "Encrypt private message?", "Encrypt?", JOptionPane.YES_NO_OPTION);
  try {
    if (encrypt == JOptionPane.YES_OPTION) {
      OneTimePad otp = new OneTimePad();
      message = "#"+otp.encrypt(message)+"#";
    }
    strOut.writeUTF("@"+recipient+"@"+message);
    strOut.flush();
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
 }

 public void Connect(String serverName, int portNum){
  
  try {
   done = false;
   socket = new Socket(serverName, portNum);
   // display (that "we got connected")...
   displayText.setText("We Are Connected\n");
   open();   
   //this.setTitle("User: " + ID+ "'s box");
   //enable our buttons....
   btnQuit.setEnabled(true);
   btnSend.setEnabled(true);
   btnConnect.setEnabled(false);
   btnPrivate.setEnabled(true);
   //either... use .setEnable... and have separate buttons and use getSource from within actionPerformed
   // or ... use .setText ... and have 1 button whose face changes and use 
   //getActionCommand from within actionPerformed
   //or... still use getSource... and compare against the boolean done
   
   
   
  } catch (UnknownHostException e) {
   e.printStackTrace();
   done = true;
   //displayMessage(""OOPSY" + e.getMessage() + ... or something nicer");

  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
   done = true;
   //displayMessage(""OOPSY" + e.getMessage() + ... or something nicer");
  }
  
  finally{
   
  }
  
 }
 
 public void send(){
  //get the message from the input textfield getText(); ... store it in String
  //make sure to 
  //String msg = input.getText();
  
  try {
   strOut.writeUTF(input.getText());
   strOut.flush();
   input.setText("");
   
   //displayText.setText("User said: " + input.getText());
   //displayText.append("\nUser said: " + input.getText());

  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  
 }
 
 public void disconnect(){
  done = true;
  try {
    strOut.writeUTF("**Disconnected**");
    strOut.flush();
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  //either setEnabled(false)... for the disconnect button
  btnPrivate.setEnabled(false);
  btnSend.setEnabled(false);
  btnConnect.setEnabled(true);
  displayText.setText("You are Disconnected");
  //if...stream is !=null
  //stream.close();
  //do that for in and for out ...do that for the socket
  try {
   close();
  } catch (IOException e) {
   
   e.printStackTrace();
  }
  
  
 }
 
 public void open(){
  try {
   strOut = new DataOutputStream(socket.getOutputStream());
   strIn = new DataInputStream(socket.getInputStream());
   new Thread(this).start(); // to be able to listen in
   
   
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  
  
 }
 
 public void close() throws IOException{
 
  try{
   if(strIn!=null){
    strIn.close();
   }
   if(strOut!=null){
    strOut.close();
   }
   if(socket!=null){
    socket.close();
   }   
   if(client != null){
    client.close();
   }
  }
  catch(IOException e){
   e.printStackTrace();
  }
  
 }
 
 public static void main(String [] args) {
  javax.swing.SwingUtilities.invokeLater(new Runnable(){
   public void run() {
    ChatClientGUI chatClientG = new ChatClientGUI();
    chatClientG.pack();
    chatClientG.setVisible(true);
    
   }
  });
 }
 
 

}
