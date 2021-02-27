package de.luuuuuis.server;

import lombok.SneakyThrows;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server extends WebSocketServer {

    private static final int PORT = 666;
    private final List<WebSocket> clients = new ArrayList<>();
    final JedisPubSub jedisPubSub = new JedisPubSub() {
        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
            System.out.println("onUnsubscribe");
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            System.out.println("onSubscribe");
        }

        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {

        }

        @Override
        public void onMessage(String channel, String message) {
            System.out.println("Message received" + message);
            clients.forEach(webSocket -> webSocket.send(message));
        }
    };
    private final Jedis subJedis;
    private final Jedis pubJedis;

    public Server(int port) {
        super(new InetSocketAddress(port));

        subJedis = new Jedis("redis", 6379);
        subJedis.auth("yeet");

        pubJedis = new Jedis("redis", 6379);
        pubJedis.auth("yeet");


        new Thread(() -> subJedis.subscribe(jedisPubSub, "test"), "subscriber").start();
    }

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        clients.add(webSocket);

        String url = webSocket.getResourceDescriptor();
        System.out.println("Connection from " + webSocket.getRemoteSocketAddress() + " to " + url);

        new Thread(() -> {
            try {
                pubJedis.publish("test", InetAddress.getLocalHost() + ": " + webSocket.getRemoteSocketAddress() + " joined to server " + InetAddress.getLocalHost());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients.remove(webSocket);

        System.out.println(webSocket.getRemoteSocketAddress() + " left");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("msg from " + webSocket.getRemoteSocketAddress() + ": " + s);
        new Thread(() -> {
            try {
                pubJedis.publish("test", InetAddress.getLocalHost() + ": " + webSocket.getRemoteSocketAddress() + ": " + s);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @SneakyThrows
    @Override
    public void onStart() {
        System.out.println("Server started at " + InetAddress.getLocalHost() + PORT);
        setConnectionLostTimeout(100);
        setMaxPendingConnections(100);
    }
}
