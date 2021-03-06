/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2013 Andune (andune.alleria@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
/**
 * 
 */
package com.andune.minecraft.commonlib;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author morganm
 *
 */
public class JarUtils {
    private static final Logger log = LoggerFactory.getLogger(JarUtils.class);
    
	private final File dataFolder;
	private final File jarFile;
	
	public JarUtils(File dataFolder, File jarFile) {
		this.dataFolder = dataFolder;
		this.jarFile = jarFile;
	}
	
	/** Code adapted from Puckerpluck's MultiInv plugin.
	 * 
	 * @param string
	 * @return
	 */
    public void copyConfigFromJar(String fileName, File outfile) throws FileNotFoundException, IOException {
        File file = new File(dataFolder, fileName);
        
        if (!outfile.canRead()) {
//            try {
            	JarFile jar = new JarFile(jarFile);
            	
                file.getParentFile().mkdirs();
                JarEntry entry = jar.getJarEntry(fileName);
                if( entry == null )
                    throw new FileNotFoundException("Couldn't locate file "+fileName+" in jar file "+jar.getName());

                InputStream is = jar.getInputStream(entry);
                FileOutputStream os = new FileOutputStream(outfile);
                byte[] buf = new byte[(int) entry.getSize()];
                is.read(buf, 0, (int) entry.getSize());
                os.write(buf);
                os.close();
//            } catch (Exception e) {
//                log.warning("Could not copy config file "+fileName+" to default location");
//            }
        }
    }

    /**
     * Return a file from the JAR as a string.
     *
     * @param fileName the file to read
     * @return the contents of the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String readFileFromJar(String fileName) throws FileNotFoundException, IOException {
        JarFile jar = new JarFile(jarFile);

        JarEntry entry = jar.getJarEntry(fileName);
        if( entry == null )
            throw new FileNotFoundException("Couldn't locate file "+fileName+" in jar file "+jar.getName());

        InputStream is = jar.getInputStream(entry);
        Reader reader = new InputStreamReader(is);
        StringWriter writer = new StringWriter((int) entry.getSize());
        char[] buf = new char[(int) entry.getSize()];
        reader.read(buf, 0, (int) entry.getSize());
        writer.write(buf);
        is.close();
        writer.close();

        return writer.toString();
    }
    
    /**
     * Return the build string from the jar manifest.
     * 
     * @return
     */
    public String getBuild() {
    	String build = "unknown";
    	
        try {
        	JarFile jar = new JarFile(jarFile);
        	Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            build = attributes.getValue("Implementation-Build");
        } catch (Exception e) {
            log.warn("Could not load build string from JAR");
        }
        
        return build;
    }
    
    /**
     * Return the version string from the jar manifest.
     * 
     * @return
     */
    public String getVersion() {
        String version = "unknown";
        
        try {
            JarFile jar = new JarFile(jarFile);
            Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            version = attributes.getValue("Implementation-Version");
        } catch (Exception e) {
            log.warn("Could not load version string from JAR");
        }
        
        return "build "+version;
    }

    /** Given a packageName, return all classes that are in this jar file that are part
     * of that package or sub packages.
     * 
     * @param packageName
     * @return
     */
    public Class<?>[] getClasses(final String packageName) {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		
    	try {
    		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    		assert classLoader != null;
    		String path = packageName.replace('.', '/');
    		JarFile jar = new JarFile(jarFile);
    		Enumeration<JarEntry> entries = jar.entries();
    		for(; entries.hasMoreElements();) {
    			final JarEntry entry = entries.nextElement();
    			final String entryName = entry.getName();
    			
//    			Debug.getInstance().devDebug("entry name=",entryName,", path=",path);
    			if( entryName.endsWith(".class") && entryName.startsWith(path) ) {
    				String className = entryName.replace("/", ".");
    				className = className.substring(0, className.length()-6);
//        			Debug.getInstance().devDebug("className=",className);
        			
    				try {
    					Class<?> clazz = Class.forName(className);
        				classList.add(clazz);
//        				Debug.getInstance().devDebug("added class: ",clazz);
    				}
    				catch(ClassNotFoundException e) {
    		    		e.printStackTrace();
    				}
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		return classList.toArray(new Class<?>[] {});
    }
}
