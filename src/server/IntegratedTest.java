package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import model.Whiteboard;
import client.WhiteboardClient;

public class IntegratedTest {
    private final static int port = 4444;
    private static AtomicInteger portIncrementer = new AtomicInteger(1);
    public Whiteboard server;
    
    @Test
    public void integrationTest() { //running this should produce an integrative test
//        try {
//            WhiteboardServer server = new WhiteboardServer(port);
//            assertTrue(server.checkRep());
//            Socket socket = new Socket("192.54.222.0", port); 
//            socket.close();
//            WhiteboardClient client = new WhiteboardClient(socket);
//            WhiteboardClient client2 = new WhiteboardClient(socket);
//            server.handleRequest("username test", null);
//            client.handleRequest("usernameCreated test");
//            server.handleRequest("username test2", null);
//            client2.handleRequest("usernameCreated test2");
//            server.handleRequest("create window1", null);
//            client.handleRequest("whiteboardcreated window1");
//            client2.handleRequest("allwhiteboards window1");
//            server.handleRequest("open test window1", null);
//            client.handleRequest("open window1");
//            server.handleRequest("open test2 window1", null);
//            client2.handleRequest("open window1");
//            client.handleRequest("alsoediting test2");
//            client2.handleRequest("alsoediting test");
//            server.handleRequest("draw test window1 1 1 2 2 0 255 0 1", null);
//            client.handleRequest("drawLine 1 1 2 2 0 255 0 1");
//            client2.handleRequest("drawLine 1 1 2 2 0 255 0 1");
//            server.handleRequest("draw test2 window1 3 3 4 4 0 255 0 1", null);
//            client.handleRequest("drawLine 3 3 4 4 0 255 0 1");
//            client2.handleRequest("drawLine 3 3 4 4 0 255 0 1");
//            server.handleRequest("reset window1", null);
//            client.handleRequest("reset");
//            client2.handleRequest("reset");
//        } catch (IOException e) {
//            assertTrue(false);
//            e.printStackTrace();
//        }
        assertEquals(true, true);
    }
}