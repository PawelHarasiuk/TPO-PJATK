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

public class SolZWykladu {
    public static void main(String[] args) throws IOException {
        Path startPath = Paths.get(System.getProperty("user.home") + "/TPO1dir");
        Path resPath = Paths.get("restFile");
        Charset csIn = Charset.forName("Cp1250");
        Charset csOut = Charset.forName("UTF-8");
        List<Path> pathList = new ArrayList<>();

        try (Stream<Path> pathStream = Files.walk(startPath)){
            pathList = pathStream.filter(Files::isRegularFile).collect(Collectors.toList());
        }

        try (FileChannel fcOut = FileChannel.open(resPath, CREATE, TRUNCATE_EXISTING, WRITE)) {
            for (Path p : pathList){
                try (FileChannel fcIn = FileChannel.open(p)){
                    ByteBuffer bb = ByteBuffer.allocateDirect((int) fcIn.size());
                    fcIn.read(bb);
                    bb.flip();
                    CharBuffer cb = csIn.decode(bb);
                    fcOut.write(csOut.encode(cb));
                }
            }
        }

    }
}
