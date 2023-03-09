package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        Path startPath = Paths.get(dirName);
        Path resPath = Paths.get(resultFileName);

        Charset charsetIn = Charset.forName("Cp1250");
        Charset charsetOut = Charset.forName("UTF-8");
        List<Path> pathList = new ArrayList<>();

        try (Stream<Path> pathStream = Files.walk(startPath)) {
            pathList = pathStream.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel fileChannelOut = FileChannel.open(resPath, CREATE, TRUNCATE_EXISTING, WRITE)) {
            for (Path path : pathList) {
                FileChannel fileChannelIn = FileChannel.open(path);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) fileChannelIn.size());
                fileChannelIn.read(byteBuffer);
                byteBuffer.flip();
                CharBuffer charBuffer = charsetIn.decode(byteBuffer);
                fileChannelOut.write(charsetOut.encode(charBuffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
