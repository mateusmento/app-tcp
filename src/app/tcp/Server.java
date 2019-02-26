package app.tcp;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;


public class Server
{
    ServerListener listener;
    ServerSocket server;
    
    public Server(ServerListener listener)
    {
        this.listener = listener;
    }
    
    public void listen(int port)
    {        
        executeInNewThread(() -> tryListenForConnections(port));
    }

    private void executeInNewThread(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    private void tryListenForConnections(int port)
    {
        try
        {
            listenForConnections(port);
        }
        catch (IOException e)
        {
            System.err.println("Error on opening socket for port " + port);
        }
    }

    private void listenForConnections(int port) throws IOException
    {
        this.server = new ServerSocket(port);

        while (true)
            tryProcessNewConnection();
    }

    private void tryProcessNewConnection()
    {
        try 
        {
            processNewConnection();
        }
        catch (IOException e)
        {
            System.err.println("Error on making new connection");
        }
    }
    
    private void processNewConnection() throws IOException
    {
        Socket connection = server.accept();
        executeInNewThread(() -> handleConnection(connection));
    }

    private void handleConnection(Socket connection)
    {
        ServerToClientConnector connector = new ServerToClientConnector(connection, listener);
        connector.talk();
    }
}
