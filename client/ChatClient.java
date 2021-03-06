// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    try {
    openConnection();
    sendToServer("#login "+loginID);
    }
    catch(Exception e) {
    	clientUI.display("Cannot open connection.  Awaiting command.");
    }
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {		
	  if(message.charAt(0) == '#') {
  		command(message);
  		}
	  else if(!isConnected()) {
		  clientUI.display("You are not connected, try logging in");
	  }
	  else {
		  try
		  {
			  
      sendToServer(message);
			  
		  }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  @Override
  protected void connectionException(Exception exception) {
	 clientUI.display("WARNING - The server has stopped listening for connections\r\n" + 
	 		"SERVER SHUTTING DOWN! DISCONNECTING!\r\n" + 
	 		"");
	 try {
	 closeConnection();
	 }
	 catch(Exception e) {}
	}
  
 protected void command(String msg){
	String message[] =  msg.split(" ");
	  switch(message[0]) {
	  case"#quit":
		  quit();
		  System.exit(0);
		 break;
	  case"#logoff":
		  try
		    {
			  clientUI.display("Logging off, closing connection now");
		      closeConnection();
		    }
		    catch(IOException e) {}
		  break;
		    
	  case"#sethost":
		  String hostname = message[1].replace("<"," ");
		  hostname = hostname.replace(">", " ");
		  setHost(hostname);		
		  break;
	  case"#setport":
		  String portname = message[1].replace("<","");
		  portname = portname.replace(">", "");
		  try {
			  int port = Integer.parseInt(portname);
			  setPort(port);
		  }catch(NumberFormatException e) {
			  clientUI.display("Integer Port number was not found");
		  }
			 break;
	  case"#login":
		  if(isConnected()) {
			  clientUI.display("Error: Client is already connected");
		  }else {
			  try {
			  openConnection();
			  sendToServer("#login "+loginID);
			  }
			  catch(Exception e){
				  clientUI.display("Cannot open connection.  Awaiting command.");
			  }
		  }
			 break;
	  case"#gethost":
		  clientUI.display("Host set to: "+ getHost());
			 break;
	  case"#getport":
		  clientUI.display("Port set to: "+ Integer.toString(getPort()));
			 break;
	  default:
			clientUI.display("Can not read command. Possible commands are:" + "\n" + "#quit, #logoff, #sethost <hostname>, #setport <portnumber>" + "\n" + "#login, #getport, #setport");
	  
	  }
  }
}
//End of ChatClient class
