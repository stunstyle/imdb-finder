package com.stunstyle.imdb.finder;

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

import com.stunstyle.imdb.finder.util.CommandParser;

public class IMDBClient {
    public static void main(String[] args) throws InterruptedException {

        try (SocketChannel sc = SocketChannel.open()) {

            InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 9989);
            sc.connect(addr);
            sc.configureBlocking(false);

            if (sc.finishConnect()) {
                System.out.println("IMDB Client running on server" + addr);
            }

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                if (!command.equals("quit")) {
                    ByteBuffer buf = ByteBuffer.allocate(8192);
                    buf.put(command.getBytes());
                    buf.flip();
                    while (buf.hasRemaining()) {
                        sc.write(buf);
                    }
                    Thread.sleep(300);
                    if (command.startsWith("get-movie-poster")) {
                        Thread.sleep(5000);
                        buf = ByteBuffer.allocate(150000);
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
                                    + CommandParser.getCommandParser().getMovieName(command) + ".jpg");
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
                    } else {
                        buf = ByteBuffer.allocate(8192);

                        while (true) {
                            buf.clear();
                            int r = sc.read(buf);
                            if (r <= 0) {
                                break;
                            }
                        }
                        System.out.println(StandardCharsets.UTF_8.decode(buf));

                    }
                } else {
                    ByteBuffer buf = ByteBuffer.wrap("quit".getBytes(StandardCharsets.UTF_8));
                    while (buf.hasRemaining()) {
                        sc.write(buf);
                    }
                    System.out.println("Disconnecting...");
                    scanner.close();
                    sc.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected.");
    }

}
