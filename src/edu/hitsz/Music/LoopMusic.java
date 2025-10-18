package edu.hitsz.Music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;

public class LoopMusic extends MusicThread{
    public volatile boolean looping = true;

    public LoopMusic(String filename) {
        super(filename);
    }

    @Override
    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SoureDataLine是可以写入数据的数据行
        SourceDataLine dataLine = null;
        //获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1 && looping) {
				//从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead =
                        source.read(buffer, 0, buffer.length);
				//通过此源数据行将数据写入混频器
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dataLine.drain();
        dataLine.close();
    }

    @Override
    public void run() {
        while (looping) { // 支持通过 interrupt() 停止循环
            try {
                InputStream stream = new ByteArrayInputStream(samples);
                play(stream);
                // 可选：在循环之间加一点延迟（避免 CPU 占用过高）
                // Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                break; // 出错就退出循环
            }
        }
    }
}