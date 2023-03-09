package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        Path dir = Paths.get(dirName);
        Path resultFile = Paths.get(resultFileName);

        try {
            if (!Files.exists(resultFile)) {
                Files.createFile(resultFile);
            }
            Files.write(resultFile, "".getBytes()); // clear the file

            Charset cp1250 = Charset.forName("Cp1250");
            Charset utf8 = Charset.forName("UTF-8");
            CharsetEncoder encoder = utf8.newEncoder();
            CharsetDecoder decoder = cp1250.newDecoder();

            FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try (FileChannel inChannel = FileChannel.open(file, StandardOpenOption.READ);
                         FileChannel outChannel = FileChannel.open(resultFile, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
                        ByteBuffer buffer = ByteBuffer.allocate((int) inChannel.size());
                        inChannel.read(buffer);
                        buffer.flip();
                        CharBuffer charBuffer = decoder.decode(buffer);
                        buffer = encoder.encode(charBuffer);
                        outChannel.write(buffer);
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            Files.walkFileTree(dir, fileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
