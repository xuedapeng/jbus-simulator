package cc.touchuan.simulator.console.handler;


import java.util.Date;

import cc.touchuan.simulator.helper.ByteHelper;
import cc.touchuan.simulator.helper.DateHelper;
import cc.touchuan.simulator.helper.HexHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DeviceConsoleHandler extends SimpleChannelInboundHandler<ByteBuf> {

	String deviceId;
	String token;
	
	public DeviceConsoleHandler(String deviceId, String token) {
		this.deviceId = deviceId;
	}

	@Override 
	public void channelActive(ChannelHandlerContext ctx) {
		
		String regInfo = String.format("REG:%s,%s;", deviceId, token);
		ctx.writeAndFlush(ByteHelper.str2bb(regInfo));
		
		System.out.println(String.format("\n[%s] active devId=%s", DateHelper.fYmdhmsms(new Date()), deviceId));
		System.out.print("\nINPUT DATA TO SERVER>"); 
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		
		System.out.println(String.format("\n[%s] recieve: %s, %s", DateHelper.fYmdhmsms(new Date()), 
				deviceId,
				HexHelper.bytesToHexString(ByteHelper.bb2bytes(msg))));
		

		System.out.print("\nINPUT DATA TO SERVER>"); 

	}

	@Override 
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause) { 
		
		cause. printStackTrace(); 
		ctx.close(); 
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {

		System.out.println(String.format("\n[%s] inactive devId=%s", DateHelper.fYmdhmsms(new Date()),
				deviceId));
	}
	
}
