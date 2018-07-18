package cc.touchuan.simulator.console;


import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import cc.touchuan.simulator.helper.CryptoHelper;
import cc.touchuan.simulator.helper.DateHelper;
import cc.touchuan.simulator.helper.HexHelper;


public class CtrlConsole {

	String host;
	int port;
	String user;
	String password;
	String deviceId;
	
	MqttClient mqtt;
	
	public CtrlConsole(String host, int port, String user, String password, String deviceId) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.deviceId = deviceId;
	}
	
	public void connect() {
		runMqtt();
	}
	
	public MqttClient mqtt() {

		int secs = 0;
		while(mqtt == null || !mqtt.isConnected()) {
			secs++;
			System.out.println(String.format("\nwait for mqtt connect...%d seconds", secs));
			System.out.print("\nSEND CMD TO DEVICE>"); 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mqtt;
	}
	

	private void runMqtt() {

		 MqttConnectOptions _connOpts = new MqttConnectOptions(); 
		 
		_connOpts.setCleanSession(true);  
		_connOpts.setUserName(user);  
		_connOpts.setPassword(password.toCharArray());  
		_connOpts.setConnectionTimeout(10);  
		_connOpts.setKeepAliveInterval(20); 
        
		try {
			mqtt = createMqttClient(_connOpts);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private  MqttClient createMqttClient(MqttConnectOptions _connOpts) throws MqttException {
		
        MemoryPersistence persistence = new MemoryPersistence();  
        
        String broker = "tcp://"+host+":"+port;
        final MqttClient mqttClient = new MqttClient(broker, CryptoHelper.genUUID(), persistence);  
        
        mqttClient.setCallback(new MqttCallback(){

        	@Override
			public void connectionLost(Throwable cause) {
				cause.printStackTrace();
			}

        	@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				
			}

        	@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				
				System.out.println(String.format("\n[%s] recieve: %s, %s", DateHelper.fYmdhmsms(new Date()),
						topic, 
						HexHelper.bytesToHexString(message.getPayload())));

				System.out.print("\nSEND CMD TO DEVICE>"); 
			}
        	
        });  
        

        System.out.println("\nmqtt server connecting...");
        mqttClient.connect(_connOpts);  
        System.out.println("\nmqtt server connected.");
		System.out.print("\nSEND CMD TO DEVICE>"); 
		
        mqttClient.subscribe("TC/DAT/"+deviceId);
        return mqttClient;  
	}
	

}
