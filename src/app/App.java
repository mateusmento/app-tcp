package app;

import app.tcp.Server;
import app.tcp.Client;
import java.util.List;
import java.util.Arrays;

public class App
{
    public static void main(String[] args)
    {
        int port1 = -1;
        int port2 = -1;
        int max = Integer.MAX_VALUE;
        String url = null;
        
        String cmdLine = String.join(" ", args);

        List <String> list = Arrays.asList(cmdLine.split("-"));
        for (String str : list)
        {
            String[] arr = str.split("=");

            if (arr.length > 1)
            {
                String cmd = arr[0].trim();
                String arg = arr[1].trim();
                
                if (cmd.equals("p1"))
                {
                     port1 = Integer.parseInt(arg);
                }
                else if (cmd.equals("p2"))
                {
                    port2 = Integer.parseInt(arg);
                }
                else if (cmd.equals("max"))
                {
                    max = Integer.parseInt(arg);
                }
                else if (cmd.equals("url"))
                {
                    url = arg;
                }
            }
        }

        if (port1 == -1 && port2 == -1)
        {
            System.out.println("No valid port was given. Aborted.");
            return;
        }
        
        new AppServer(port1, port2).launch(max);
        
        if (url != null)
        {
            Client client = Client.connect(url);
            if (client != null) client.send("-1");
        }
    }
}
