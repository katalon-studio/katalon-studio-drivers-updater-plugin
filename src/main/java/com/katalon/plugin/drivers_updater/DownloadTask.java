package com.katalon.plugin.drivers_updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;

//Download browser drivers.
//By Anh Tuan

public class DownloadTask {
	private String url;
	
	public DownloadTask(String url){
		this.url = url;
	}
	
	File downloadAndExtract(File targetDir) throws IOException {
        URL url = new URL(this.url);

        if (this.url.contains(".zip")){
        	return unpackArchive(url, targetDir);
        } else{
        	return downloadFile(url, targetDir);
        }
    }
    
	public File downloadFile(URL url, File targetDir) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        File file = targetDir;
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = this.url.substring(this.url.lastIndexOf("/") + 1, this.url.length());

            InputStream inputStream = httpConn.getInputStream();
            file = new File(targetDir,  File.separator +  fileName);
             
            FileOutputStream outputStream = new FileOutputStream(file);
 
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();

        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        
        return file.getCanonicalFile();
    }
	
    private File unpackArchive(URL url, File targetDir) throws IOException{
    	url.openStream();

        InputStream in = new BufferedInputStream(url.openStream(), 1024);
        ZipInputStream inputStream = new ZipInputStream(in);
    	
        ZipEntry entry;
        File file = targetDir;

        while((entry = inputStream.getNextEntry()) != null){
        	String folder = entry.getName();
        	
            file = new File(targetDir, File.separator + folder);

            if(!buildDirectory(file.getParentFile())){
                throw new IOException("Could not create directory: " + file.getParentFile());
            }

            if(!entry.isDirectory()){
                copyInputStream(inputStream, new BufferedOutputStream(new FileOutputStream(file)));
            } else{
                if(!buildDirectory(file))
                {
                    throw new IOException("Could not create directory" + file);
                }
            }
        }
        return file.getCanonicalFile();
    }

    private void copyInputStream(InputStream in, OutputStream out) throws IOException {
        IOUtils.copy(in, out);
        out.close();
    }
    
    private boolean buildDirectory(File file)
    {
        return file.exists() || file.mkdirs();
    }
}
