package cc.touchuan.simulator;

import java.util.Date;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import cc.touchuan.simulator.console.CtrlConsole;
import cc.touchuan.simulator.console.DeviceConsole;
import cc.touchuan.simulator.helper.ByteHelper;
import cc.touchuan.simulator.helper.DateHelper;
import cc.touchuan.simulator.helper.HexHelper;
import io.netty.channel.Channel;

public class AppConsole {
	
	public static void main(String[] args) {
		
		String type = args[1];
		if (type.equals("d")) {
			String host = args[2];
			int port = Integer.valueOf(args[3]);
			String deviceId = args[4];
			String token = args[5];
			runDeviceConsole(host, port, deviceId, token);
			
			
		} else if(type.equals("c")) {
			String host = args[2];
			int port = Integer.valueOf(args[3]);
			String user = args[4];
			String password = args[5];
			String deviceId = args[6];
			runCtrlConsole(host, port, user, password, deviceId);
			
		} else {
			System.out.println("wrong console type.");
			return;
		}
	}
	
	private static void runDeviceConsole(String host, int port, String deviceId, String token) {
		DeviceConsole console = new DeviceConsole(host, port, deviceId, token);
		console.connect();
		
		Channel channel = console.channel();

		Scanner scanner = new Scanner(System.in);  
		scanner.useDelimiter("\n");
		while (true) {  
			System.out.print("\nINPUT DATA TO SERVER>"); 
			String in = scanner.next();
			in = in.replaceAll("\\s+", "");

			if (in.startsWith("exit")) {
				System.out.println("bye...");
				channel.close();
				System.exit(0);
			}

			byte[] data = HexHelper.hexStringToBytes(in);
			
			if (data == null || data.length == 0) {
				continue;
			}

			System.out.println(String.format("[%s] channel.isWritable=%s", DateHelper.fYmdhmsms(new Date()), 
					channel.isWritable())); 
			System.out.println(String.format("[%s] send data:%s" , DateHelper.fYmdhmsms(new Date()),
					HexHelper.bytesToHexString(data)));
			
			channel.writeAndFlush(ByteHelper.bytes2bb(data));
			
		}  
		
	}

	private static void runCtrlConsole(String host, int port, String user, String password, String deviceId) {
		CtrlConsole console = new CtrlConsole(host, port, user, password, deviceId);
		console.connect();
		
		MqttClient mqtt = console.mqtt();

		Scanner scanner = new Scanner(System.in);  
		scanner.useDelimiter("\n");
		while (true) {  
			System.out.print("\nSEND CMD TO DEVICE>"); 
			String in = scanner.next();
			in = in.replaceAll("\\s+", "");
			
			if (in.startsWith("exit")) {
				System.out.println("bye...");
				try {
					mqtt.disconnect();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}

			byte[] cmd = HexHelper.hexStringToBytes(in);
			
			if (cmd == null || cmd.length == 0) {
				continue;
			}

			System.out.println(String.format("[%s] mqtt.isConnected=%s", DateHelper.fYmdhmsms(new Date()), 
					mqtt.isConnected())); 
			System.out.println(String.format("[%s] send cmd:%s", DateHelper.fYmdhmsms(new Date()), 
					HexHelper.bytesToHexString(cmd)));
			
			MqttMessage mm = new MqttMessage(cmd);
			try {
				mqtt.publish("TC/CMD/"+deviceId, mm);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}  
		
	}
}
