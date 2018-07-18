package cc.touchuan.simulator.console;

import java.net.InetSocketAddress;

import cc.touchuan.simulator.console.handler.DeviceConsoleHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DeviceConsole {

	String host;
	int port;
	String deviceId;
	String token;
	
	Channel channel;
	
	public DeviceConsole(String host, int port, String deviceId, String token) {
		this.host = host;
		this.port = port;
		this.deviceId = deviceId;
		this.token = token;
	}
	
	public void connect() {
		new Thread() {
			@Override
			public void run() {
				try {
					socketStart();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public Channel channel()  {
		
		int secs = 0;
		while(channel == null) {
			secs++;
			System.out.println(String.format("wait for channel create...%d seconds", secs));
			System.out.println("\nINPUT DATA TO SERVER>"); 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return channel;
	}
	

	private void socketStart() throws Exception { 

		System.out.println(String.format("connecting... devId=%s", deviceId));
		System.out.println("\nINPUT DATA TO SERVER>"); 
		
		EventLoopGroup group = new NioEventLoopGroup(); 
		try { 
			Bootstrap b = new Bootstrap();
			b.group( group).channel(NioSocketChannel.class) 
				.remoteAddress(new InetSocketAddress(host, port)) 
				.handler(
						new ChannelInitializer<SocketChannel>() { 
							@Override 
							public void initChannel(SocketChannel ch) throws Exception { 
								ch. pipeline(). addLast( new DeviceConsoleHandler(deviceId, token)); 
							} 
						}); 

			ChannelFuture cf = b.connect().sync(); // 连接到远程节点，阻塞等待直到连接完成 
			
			channel = cf.channel();
			cf.channel().closeFuture().sync(); //阻塞，直到 Channel 关闭 
			
	        
		} finally { 
			group. shutdownGracefully(). sync(); //关闭线程池并且释放所有的资源 
		} 
	} 
}
