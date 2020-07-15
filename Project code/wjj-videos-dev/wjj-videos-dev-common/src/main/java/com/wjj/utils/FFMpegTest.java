package com.wjj.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

	private String ffmpegEXE;
	
	public FFMpegTest(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor (String videoInPutPath,String videoOutPutPath)throws Exception {
		//$ ffmpeg -i input.mp4 output.avi
		List<String> command=new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInPutPath);
		command.add(videoOutPutPath);
		
		for(String c:command) {
			System.out.print(c);
		}
		ProcessBuilder builder=new ProcessBuilder(command);
		Process process=builder.start();
		InputStream errorStream=process.getErrorStream();
		InputStreamReader inputStreamReader=new InputStreamReader(errorStream);
		BufferedReader br=new BufferedReader(inputStreamReader);
		
		String line="";
		while((line=br.readLine())!=null) {
			
		}
		if(br!=null) {
			br.close();
		}
		if(errorStream!=null) {
			errorStream.close();
		}
		if(inputStreamReader!=null) {
			inputStreamReader.close();
		}
	}

	public static void main(String[] args) {
		FFMpegTest ffmpeg=new FFMpegTest("E:\\ffmpeg\\bin\\ffmpeg.exe");
		try {
			ffmpeg.convertor("E:\\2.mp4", "E:\\3.avi");
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

}
