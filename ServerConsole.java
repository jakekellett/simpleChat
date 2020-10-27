
import java.util.Scanner;

import common.ChatIF;


public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	 Scanner fromConsole; 
	 EchoServer server;
	 
	 public ServerConsole(int port) 
	  {
		 
	      this.server = new EchoServer(port,this);
	      try {
	    	  server.listen();
	      }catch(Exception ex) {
	    	  System.out.println("ERROR - Could not listen for clients!");
	      }
	    
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }
	 
	 public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	 public void display(String message) {
		 System.out.println("> " + message);
	 }
	 
	 
	 public static void main(String[] args) 
	  {
	    int port;
	    String argument;

	    try
	    {
	    	argument = args[0];
	    	try {
	    		port = Integer.parseInt(argument);
	    	}
	    	catch(NumberFormatException e){
	    		port = DEFAULT_PORT;
	    	}
	    	
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	    	port = DEFAULT_PORT;
	    }
	    ServerConsole serverConsole = new ServerConsole(port);
	    serverConsole.accept();  //Wait for console data
	  }
}

