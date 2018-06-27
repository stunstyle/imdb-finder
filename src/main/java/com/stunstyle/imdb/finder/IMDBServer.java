package com.stunstyle.imdb.finder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class IMDBServer implements AutoCloseable {
    private static final int SERVER_PORT = 9989;
    private static final int MAX_MESSAGE_SIZE = 8192;
    private Selector selector;

    public IMDBServer(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ServerSocket ss = ssc.socket();
        InetSocketAddress addr = new InetSocketAddress(port);
        ss.bind(addr);

        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void start() throws IOException {
        System.out.println("IMDB Server running!");
        while (true) {
            int readyChannels = selector.select();

            if (readyChannels == 0)
                continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                }
                keyIterator.remove();
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        SocketChannel sc = (SocketChannel) key.channel();
        while (true) {
            buf.clear();
            int r = sc.read(buf);
            if (r <= 0) {
                break;
            }
        }
        String command = StandardCharsets.UTF_8.decode(buf).toString().trim();
        if (!command.equals("quit")) {
            buf = ByteBuffer.wrap(new IMDBCommandProcessor(command).processCommand());
            while (buf.hasRemaining()) {
                sc.write(buf);
            }
        } else {
            sc.close();
            System.out.println("Client " + sc + " disconnected!");
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        System.out.println("Client " + sc + " connected!");
    }

    @Override
    public void close() {
        try {
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            // nothing we can do
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (IMDBServer mainServer = new IMDBServer(SERVER_PORT)) {
            mainServer.start();
        } catch (IOException e) {
            System.err.println("ERROR: could not create server");
            e.printStackTrace();
        }
    }

}
