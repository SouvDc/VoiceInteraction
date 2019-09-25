package com.cnbot.irobotvoice.ali.api;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class AudioPlayer {
    private static String TAG = "AudioPlayer";
    private final int SAMPLE_RATE = 16000;
    private boolean playing = false;
    private LinkedBlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue();

    // 初始化播放器
    private int iMinBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    private AudioTrack audioTrack = new AudioTrack(
            new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            new AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            iMinBufSize,
            AudioTrack.MODE_STREAM,  AudioManager.AUDIO_SESSION_ID_GENERATE
    );

    private byte[] tempData;

    private Thread ttsPlayerThread;


    AudioPlayer(){
        Log.i(TAG,"init");
        playing = true;
        ttsPlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (playing) {
                        tempData = audioQueue.poll();
                        if (tempData == null) {
                            Thread.sleep(20);
                        } else {
                            if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                                Log.d(TAG, "audioTrack.play");
                                audioTrack.play();
                            }
                            Log.d(TAG, "audioTrack.write");
                            audioTrack.write(tempData, 0, tempData.length);
                        }
                    }
                    Log.d(TAG, "playing thread end");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ttsPlayerThread.start();
    }

    public void setAudioData(byte[] data){
        // "加入音頻數據到隊列中";
        boolean isAdd = audioQueue.offer(data);
        Log.d(TAG, "data enqueue : " + isAdd + "    audioQueue size = " + audioQueue.size());
        //非阻塞
    }

    public void stop(){
        try {
//        playing = false;
            if(audioQueue != null) {
                audioQueue.clear();
            }
            if(audioTrack != null) {
                audioTrack.pause();
                audioTrack.flush();
                audioTrack.stop();
            }
            Log.d(TAG, "stopped");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 停止音频流播放线程
     */
    public void release(){
        playing = false;
        if(ttsPlayerThread != null){
            ttsPlayerThread.interrupt();
            ttsPlayerThread = null;
        }
        audioTrack = null;
    }
}
