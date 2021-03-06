package com.kor.admiralty.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.SortedMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconLoader {
	
	public static void loadCachedIcons(File file, SortedMap<String, ImageIcon> cache) {
		if (!file.exists()) return;
		try {
			ZipFile zipFile = new ZipFile(file);
			try {
				for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();) {
					ZipEntry entry = e.nextElement();
					String name = entry.getName();
					InputStream inStream = zipFile.getInputStream(entry);
					BufferedImage image = ImageIO.read(inStream);
					ImageIcon icon = new ImageIcon(image);
					cache.put(name, icon);
				}
			} finally {
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveCachedIcons(File file, SortedMap<String, ImageIcon> cache) {
		try {
			FileOutputStream outStream = new FileOutputStream(file);
			ZipOutputStream zipStream = new ZipOutputStream(outStream);
			
			for (Map.Entry<String, ImageIcon> entry : cache.entrySet()) {
				try {
					String name = entry.getKey();
					ZipEntry zipEntry = new ZipEntry(name);
					zipStream.putNextEntry(zipEntry);
				
					ImageIcon icon = entry.getValue();
					BufferedImage image = (BufferedImage)icon.getImage();
					ImageIO.write(image, "png", zipStream);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					zipStream.closeEntry();
				}
			}
			
			zipStream.flush();
			zipStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
