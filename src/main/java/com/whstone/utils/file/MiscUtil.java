package com.whstone.utils.file;

import cn.hutool.core.date.DateUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhongkf on 2019/07/19
 */
public final class MiscUtil {
 
    private static Map<String, String> configProperty = null;
    
	 
	/**
	 * 从configPropertyPath 文件中有key获取values
	 * @param key
	 * @return
	 */
	public static String getConfigByKey(String key,String configPropertyPath) {
		if(isEmptyString(key)){ return null;		}
		try{
			if(configProperty==null){
				configProperty= MiscUtil.convertPropertiesFileToMap(configPropertyPath);
			}
		} catch(Exception e){
			//TODO add log info
			e.printStackTrace();
		}
		return configProperty.get(key).trim();
	}
	
	public static boolean isEmptyString(String value){
		return value==null?true:(value.trim().length()<1?true:false);
	}
	
  
 
	public static Map<String, String> convertPropertiesFileToMap(String path) throws Exception{
		InputStream input = null;
		Map<String,String> propertiesMap = null;
		try{
			if(path.toLowerCase().startsWith("classpath:")){
				String fileName = path.substring("classpath:".length());
				input = MiscUtil.class.getClassLoader().getResourceAsStream(fileName);
				if(input==null){
					path=MiscUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
					path=path.substring(0, path.lastIndexOf(File.separatorChar)+1)+fileName;
				}
			}
			if(input==null){
				input = new FileInputStream(path);
			}
			
			Properties properties = new Properties();
			properties.load(new InputStreamReader(input, "UTF-8"));
			propertiesMap = new HashMap<String,String>();
			for(String name : properties.stringPropertyNames()){
				propertiesMap.put(name, properties.getProperty(name));
			}			
		}finally{
			if(input!=null){
				input.close();
			}
		}
		return propertiesMap;
	}

	/**
	 * 修改 map 中所有键值对到指定配置文件中
	 * @param path map
	 * @return
	 * @throws Exception
	 */
	public static boolean updateProperties(String path, Map<String,String> map) {
		InputStream input = null;
		try{
			if(path.toLowerCase().startsWith("classpath:")){
				String fileName = path.substring("classpath:".length());
				input = MiscUtil.class.getClassLoader().getResourceAsStream(fileName);
				if(input==null){
					path=MiscUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
					path=path.substring(0, path.lastIndexOf(File.separatorChar) + 1 ) + fileName;
				}
			}
			if(input==null){
				input = new FileInputStream(path);
			}

			Properties properties = new Properties();
			properties.load(new InputStreamReader(input, "UTF-8"));

			OutputStream fos = new FileOutputStream(path);
			for(String key : map.keySet()) {

				properties.setProperty(key, map.get(key));

			}
			properties.store(fos, "Update value " + DateUtil.now());
			fos.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Map map = new HashMap<>();
		map.put("inspect-server-active","2323232323232323");
		map.put("ebackup-server-ip","323");
		MiscUtil.updateProperties(System.getProperty("user.dir") +
				File.separator + "config" + File.separator + "server-config.properties",map);
	}
   
 
}
