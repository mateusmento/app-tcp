package app;

import app.tcp.Client;
import app.tcp.Server;

public class AppServer
{
    Server server;
    String clientUrl;
    int MAX = 5;
    
    public AppServer(int port, int connection_port)
    {
        this.clientUrl = "localhost:" + connection_port;
        this.server = new Server((data) -> listenToConnections(data));
    }
    
    public void launch(int port)
    {        
        server.listen(port);
    }
    
    private String listenToConnections(String data)
    {
        int num = Integer.parseInt(data) + 1;
        data = Integer.toString(num);

        if (num < MAX)
        {
            Client client = Client.connect(this.clientUrl);
            client.send(data);
            client.response((response) -> {
                int n = Integer.parseInt(response);
                System.out.println(n);
            });
        }

        return data;
    }
}
