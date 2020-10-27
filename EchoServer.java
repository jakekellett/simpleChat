// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 



import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverConsole;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverConsole) 
  {
    super(port);
    this.serverConsole = serverConsole;
  }
  
  public EchoServer(int port)
  {
	  super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	if(serverConsole == null) {
		System.out.println("Message received: " + msg + " from " + client);
	}else {
    serverConsole.display("Message received: " + msg + " from " + client);
	}
	String [] msgArray = (msg.toString()).split(" ");
	
	if(msgArray[0].equals("#login")) {
		if(client.getInfo("loginID") == null) {
			client.setInfo("loginID", msgArray[1]);
		}
		else {
			try {
				client.sendToClient("You are already logged in. Terminating your connection now");
				client.close();
			}
			catch (Exception e) {}
			
		}
	
	}else {
		String loginID = (client.getInfo("loginID")).toString();		
		msg = loginID + " - " + msg;
		this.sendToAllClients(msg);
	}
  
  }
  public void handleMessageFromServerUI(String message) {
	  if(message.charAt(0) == '#') {
  		command(message);
  	}else {

  		message = "SERVER MSG> " + message;
      	sendToAllClients(message);
  	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  if(serverConsole == null) {
      System.out.println("Server listening for connections on port " + getPort());
	  }else {
		  serverConsole.display("Server listening for connections on port " + getPort());
	  }
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  if(serverConsole == null) {
		  System.out.println("Server has stopped listening for connections.");
	  }else {
      serverConsole.display("Server has stopped listening for connections.");
	  }
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  sendToAllClients("A new client has connected");
  }
  
  
  
  @Override
  protected void clientDisconnected(
		    ConnectionToClient client) {
	  sendToAllClients("A client has disconnected");
	  
  }
  public void command(String msg) {
		 String message[] =  msg.split(" ");
		 switch(message[0]) {
		 	case"#quit":
		 		try {
		 		close();
		 		System.exit(0);
		 		}
		 		catch(Exception e) {}
		 		System.exit(0);
		 		break;
		 	case"#stop":
		 		if(isListening()){
		 		stopListening();
		 		}
		 		else {
		 		serverConsole.display("Server not listening.");
		 		}
		 		break;
		 	case"#start":
		 		if(!isListening()) {
		 			try {
		 			listen();
		 			}
		 			catch(Exception e) {}
		 		}else {
		 			serverConsole.display("Server is not listening.");
		 		}
		 		break;
		 	case"#close":
		 		try {
		 		close();
		 		}
		 		catch(Exception e) {}
		 		break;
		  	case"#setport":
		  		String portname = message[1].replace("<","");
		  		portname = portname.replace(">", "");
		  		try {
		  			int port = Integer.parseInt(portname);
		  			setPort(port);
		  		}catch(NumberFormatException e) {
		  			serverConsole.display("Integer Port number was not found");
			  }
		  		break;
		  	 case"#getport":
		  		serverConsole.display(Integer.toString(getPort()));
		  		break;
		  	default:
		  		serverConsole.display("Can not read command. Possible commands are:" + "\n" + "#quit, #close, #start, #stop, #setport <portnumber>, #getport");
		 }
	 }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
