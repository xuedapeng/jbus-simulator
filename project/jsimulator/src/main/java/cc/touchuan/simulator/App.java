package cc.touchuan.simulator;

import cc.touchuan.simulator.plugin.loadtestor.LoadTestor;

public class App {

	public static void main(String[] args) throws Exception {

		System.out.println("exe type:" + args[0]);
//    	args = new String[]{"loadtest", "20", "5"};
    	if (args.length > 0 && args[0].equals("loadtest")) {
    		
    		if (args.length == 3) {
        		LoadTestor.main(args[1],args[2]);
    		} else {
        		LoadTestor.run(args);
    		}
    		return;
    	}
    	
//		args = new String[]{"console", "d", "jbus.bizmsg.cn", "2883", "100001", "100001pwd"};
//		args = new String[]{"console", "c", "pms.bizmsg.net", "1883", "jbus", "jbus", "100001"};
		
		if (args.length > 0 && args[0].equals("console")) {
			AppConsole.main(args);
			return;
		}
		
		AppSimulator.main(args);

	}

}
