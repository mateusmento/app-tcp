package app;

import app.tcp.Client;
import app.tcp.Server;

public class AppServer
{
    Server server;
    String url;
    
    public AppServer(int port, int connection_port)
    {
        this.url = "localhost:" + connection_port;
        this.server = new Server(port);
    }
    
    public void launch(int MAX)
    {        
        server.listen((data) -> {
            int num = Integer.parseInt(data) + 1;
            data = Integer.toString(num);

            if (num < MAX)
            {
                Client client = Client.connect(this.url);
                client.send(data);
                client.response((response) -> {
                    int n = Integer.parseInt(response);
                    System.out.println(n);
                });
            }
            
            return data;
        });
    }
}
