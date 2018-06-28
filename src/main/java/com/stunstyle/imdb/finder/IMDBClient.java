package com.stunstyle.imdb.finder;

import com.stunstyle.imdb.finder.util.CommandParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Scanner;

public class IMDBClient {
    private static final int MAX_MESSAGE_SIZE = 8192;
    private static final int MAX_IMAGE_SIZE = 131072;       // 2^17

    private static void downloadPoster(SocketChannel sc, String movieName) throws InterruptedException, IOException {
        Thread.sleep(5000);
        ByteBuffer buf = ByteBuffer.allocate(MAX_IMAGE_SIZE);
        buf.clear();
        while (true) {
            int r = sc.read(buf);
            if (r <= 0) {
                break;
            }
        }
        buf.flip();
        if (buf.hasRemaining()) {
            Path myPath = Paths.get("client//poster//"
                    + movieName + ".jpg");
            Files.createDirectories(myPath.getParent());
            if (Files.notExists(myPath)) {
                Files.createFile(myPath);

                FileChannel fc = FileChannel.open(myPath, EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
                while (buf.hasRemaining()) {
                    fc.write(buf);
                }
                fc.close();

                System.out.println("Poster downloaded!");
            } else {
                System.out.println("Poster already in cache!");
            }
        } else {
            System.out.println("Movie not found!");
        }
    }

    private static String readCommandResult(SocketChannel sc) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(8192);
        while (true) {
            buf.clear();
            int r = sc.read(buf);
            if (r <= 0) {
                break;
            }
        }
        return StandardCharsets.UTF_8.decode(buf).toString();
    }

    private static void connectToServer(SocketChannel sc, String hostname, int port) throws IOException {
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 9989);
        sc.connect(addr);
        sc.configureBlocking(false);
        while (!sc.finishConnect()) {
            // waiting to connect
        }
        System.out.println("IMDB Client running on server" + addr);
    }

    private static void sendCommandToServer(SocketChannel sc, String command) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        buf.put(command.getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            sc.write(buf);
        }

    }
    public static void main(String[] args) throws InterruptedException {

        try (SocketChannel sc = SocketChannel.open()) {


            connectToServer(sc, "127.0.0.1", 9989);

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String command = scanner.nextLine();
                    if (!command.equals("quit")) {
                        sendCommandToServer(sc, command);
                        Thread.sleep(500);
                        if (command.startsWith("get-movie-poster")) {
                            String movieName = CommandParser.getCommandParser().getMovieName(command);
                            downloadPoster(sc, movieName);
                        } else {
                            String result = readCommandResult(sc);
                            System.out.println(result);
                        }
                    } else {
                        sendCommandToServer(sc, "quit");
                        System.out.println("Disconnecting...");
                        sc.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected.");
    }

}
