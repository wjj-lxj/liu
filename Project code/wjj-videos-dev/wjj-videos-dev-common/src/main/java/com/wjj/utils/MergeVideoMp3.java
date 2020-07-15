package com.wjj.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor (String videoInPutPath,String mp3InPutPath,
		double seconds,	String videoOutPutPath)throws Exception {
		//ffmpeg.exe -i 3.mp4 -i bgm.mp3 -t 15 -y xin.mp4
		List<String> command=new ArrayList<>();
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInPutPath);
		
		command.add("-i");
		command.add(mp3InPutPath);
		
		command.add("-t");
		command.add(String.valueOf(seconds));
		
		command.add("-y");
		command.add(videoOutPutPath);
		
//		for(String c:command) {
//			System.out.print(c);
//		}
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
		
		if(inputStreamReader!=null) {
			inputStreamReader.close();
		}
		if(errorStream!=null) {
			errorStream.close();
		}
	}

	public static void main(String[] args) {
		MergeVideoMp3 ffmpeg=new MergeVideoMp3("E:\\ffmpeg\\bin\\ffmpeg.exe");
		try {
			ffmpeg.convertor("E:\\3.mp4","E:\\bgm.mp3", 15.1,"E:\\新视频.mp4");
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

}
