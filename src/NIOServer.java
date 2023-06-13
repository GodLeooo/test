import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
 
public class NIOServer {
 
    public static void main(String[] args) throws IOException {
 
        // 创建Selector
        Selector selector = Selector.open();
 
        // 创建ServerSocketChannel，并绑定端口
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8888));
        serverChannel.configureBlocking(false);
 
        // 注册ServerSocketChannel到Selector上
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
 
        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) continue;
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
 
                if (key.isAcceptable()) {
 
                    // 接受客户端连接请求，并将其注册到Selector上
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);

 
                } else if (key.isReadable()) {
 
                    // 读取客户端发送的数据
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBytes = clientChannel.read(buffer);
 
                    if (readBytes > 0) {
                        // 处理读取到的数据
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String request = new String(bytes, StandardCharsets.UTF_8);
                        System.out.println("Received request: " + request);
 
                        // 回复客户端
                        String response = "Response from server.";
                        buffer.clear();
                        buffer.put(response.getBytes());
                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                }
                it.remove();
            }
        }
    }
}
