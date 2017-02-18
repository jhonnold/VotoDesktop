package utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class CommandParser {
	
	public static void parseCommand(Object obj, ArrayList<String> kwargs) {
		
		try {
			Method method = obj.getClass().getMethod(kwargs.get(0), ArrayList.class); 
			method.invoke(obj, kwargs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
