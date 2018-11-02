package app.tcp;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Client
{
    String ip;
    int port;
    private Socket connection;
    
    private Client(String ip, int port) throws IOException
    {
        this.ip = ip.replaceAll("localhost", "127.0.0.1");
        this.port = port;
        this.connection = new Socket(this.ip, this.port);
    }

    public static Client connect(String url)
    {
        String[] partials = url.split(":");
        int defaultPort = 80;
        if (partials.length == 0) return null;
        else if (partials.length == 1) return Client.connect(partials[0], defaultPort);
        else {
            int port = Integer.parseInt(partials[1]);
            return Client.connect(partials[0], port);
        }
    }
    
    public static Client connect(String ip, int port)
    {
        Client client = null;
        try {
            client = new Client(ip, port);
        } catch (IOException e) {
            System.out.println("Error on connecting to '" + ip + ":" + port + "': " + e);
        }
        return client;
    }

    public void send(String data)
    {
        new Thread(() -> {

            DataOutputStream output = null;

            if (data == null) return; 

            try {
                output = new DataOutputStream(connection.getOutputStream());
            } catch (IOException e) {
                System.out.println("Error on creating DataStream: " + e);
            }

            try {
                if (output != null) output.writeUTF(data);
            } catch (IOException e) {
                System.out.println("Error on sending output data: " + e);
            }
            
        }).start();
    }

    public void response(ClientListener listener)
    {
        new Thread(() -> {

            DataInputStream input = null;
            String dataInput = "";

            try {
                input = new DataInputStream(connection.getInputStream());
            } catch (IOException e) {
                System.out.println("Error on creating DataStream: " + e);
            }

            try {
                if (input != null) dataInput = input.readUTF();
            } catch (IOException e) {
                System.out.println("Error on getting input data: " + e);
            }
            
            if (listener != null) listener.listen(dataInput);

            this.close();
        }).start();
    }

    public void close()
    {
        try {
            connection.close();
        } catch(IOException e) {
            System.out.println("Error on port " + this.port + " closing connection to port " + connection.getPort() + ":\n\t" + e);
        }
    }
}
