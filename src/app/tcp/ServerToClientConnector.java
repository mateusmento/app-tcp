package app.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerToClientConnector
{
    Socket connection;
    ServerListener listener;

    public ServerToClientConnector(Socket connection, ServerListener listener)
    {
        this.connection = connection;
        this.listener = listener;
    }

    public void talk()
    {
        tryMakeTalk();
    }
    
    private void tryMakeTalk()
    {
        try
        {
            makeTalk();
        }
        catch (IOException e)
        {
            System.out.println("Error on creating DataStream: " + e);
            return;
        }
    }
    
    private void makeTalk() throws IOException
    {
        String info = listen(connection);

        String response = process(info);

        respond(connection, response);
    }    
    
    private String listen(Socket connection)
    {
        DataInputStream input = getInputSource(connection);
        return readInput(input);
    }
    
    private DataInputStream getInputSource(Socket connection)
    {
        try {
            return new DataInputStream(connection.getInputStream());
        } catch (IOException e) {
            System.out.println("Error on creating InputDataStream: \n" + e);
            return null; // new DataInputStream(new SequenceInputStream());
        }
    }

    private String readInput(DataInputStream input)
    {
        String inputData = "";

        try {
            inputData = input.readUTF();
        } catch (IOException e) {
            System.out.println("Error on getting input data: " + e);
        }

        return inputData;
    }
    
    private String process(String info)
    {
        String outputData = "";
        
        try {
            outputData = listener.listen(info);
        } catch (Exception e) {
            System.out.println("Listener Error: " + e);
        }
        
        return outputData;
    }

    private void respond(Socket connection, String responseData) throws IOException
    {
        DataOutputStream output = getOutputSource(connection);
        trySendData(output, responseData);
    }

    private DataOutputStream getOutputSource(Socket connection) throws IOException
    {
        return new DataOutputStream(connection.getOutputStream());
    }

    private void trySendData(DataOutputStream output, String data)
    {
        try {
            output.writeUTF(data);
        } catch (IOException e) {
            System.out.println("Error on sending output data: " + e);
        }
    }
}
