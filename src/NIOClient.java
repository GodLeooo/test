import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        // 创建SocketChannel，并连接到服务端
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8888));

        // 发送数据给服务端
        String request = "Request from client1.";
        while (true) {
            ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
            channel.write(buffer);
            Thread.sleep(1000);
        }

//        // 接收服务端回复的数据
//        buffer.clear();
//        int readBytes = channel.read(buffer);
//        if (readBytes > 0) {
//            buffer.flip();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            String response = new String(bytes, "UTF-8");
//            System.out.println("Received response: " + response);
//        }
//
//        // 关闭连接
//        channel.close();
    }
}
