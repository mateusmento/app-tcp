package app.tcp;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Server
{
    ServerSocket server = null;
    private int port;
    private boolean isListening = false;
    
    public Server(int port)
    {
        this.port = port;
    }
    
    public void listen(ServerListener listener)
    {        
        new Thread(() -> {

            try (ServerSocket server = new ServerSocket(this.port))
            {
                this.server = server;

                while (true)
                {
                    Socket connection = server.accept();
                    
                    new Thread(() -> {

                        DataInputStream input = null;
                        DataOutputStream output = null;

                        String dataInput = "";
                        String dataOutput = "";

                        try {
                            input = new DataInputStream(connection.getInputStream());
                            output = new DataOutputStream(connection.getOutputStream());
                        } catch (IOException e) {
                            System.out.println("Error on creating DataStream: " + e);
                        }

                        try {
                            if (input != null) dataInput = input.readUTF();
                        } catch (IOException e) {
                            System.out.println("Error on getting input data: " + e);
                        }

                        try {
                            String data = listener.listen(dataInput);
                            if (data != null) dataOutput = data;
                        } catch (Exception e) {
                            System.out.println("Error on listening port " + this.port + ": " + e);
                        }

                        try {
                            if (output != null) output.writeUTF(dataOutput);
                        } catch (IOException e) {
                            System.out.println("Error on sending output data: " + e);
                        }

                        try {
                            connection.close();
                        } catch(IOException e) {
                            System.out.println("Error on port " + this.port + " closing connection to port " + connection.getPort() + ":\n\t" + e);
                        }

                    }).start();
                }

            } catch(IOException e) {
                System.out.println("Error on server: " + e);
            }

            this.server = null;
        }).start();
    }

    public void close()
    {
        try {
            if (server != null) server.close();
        } catch (IOException e) {
            System.out.println("Error on closing port " + this.port + ": " + e);
        }
    }
}
