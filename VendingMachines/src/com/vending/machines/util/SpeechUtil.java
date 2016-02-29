package com.vending.machines.util;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.vending.machines.MyApplication;

public class SpeechUtil implements SpeechSynthesizerListener {
    private static SpeechUtil mInstance = new SpeechUtil();

    private SpeechSynthesizer mSpeechSynthesizer;

    private String mFolderPath;

    private SpeechUtil() {
        initialEnv();
        initialTts();
        initParams();
    }

    public static SpeechUtil getInstance() {
        return mInstance;
    }

    private void initialEnv() {
        mFolderPath = FileAccessor.BDTTSROOT;
        FileAccessor.makeDir(mFolderPath);

        FileAccessor.copyFromAssetsToSdcard(false,
                FileAccessor.SPEECH_FEMALE_MODEL_NAME, mFolderPath + "/"
                        + FileAccessor.SPEECH_FEMALE_MODEL_NAME);
        FileAccessor.copyFromAssetsToSdcard(false,
                FileAccessor.SPEECH_MALE_MODEL_NAME, mFolderPath + "/"
                        + FileAccessor.SPEECH_MALE_MODEL_NAME);
        FileAccessor.copyFromAssetsToSdcard(false,
                FileAccessor.TEXT_MODEL_NAME, mFolderPath + "/"
                        + FileAccessor.TEXT_MODEL_NAME);
        FileAccessor.copyFromAssetsToSdcard(false,
                FileAccessor.LICENSE_FILE_NAME, mFolderPath + "/"
                        + FileAccessor.LICENSE_FILE_NAME);
        FileAccessor.copyFromAssetsToSdcard(false, "english/"
                + FileAccessor.ENGLISH_SPEECH_FEMALE_MODEL_NAME, mFolderPath
                + "/" + FileAccessor.ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        FileAccessor.copyFromAssetsToSdcard(false, "english/"
                + FileAccessor.ENGLISH_SPEECH_MALE_MODEL_NAME, mFolderPath
                + "/" + FileAccessor.ENGLISH_SPEECH_MALE_MODEL_NAME);
        FileAccessor.copyFromAssetsToSdcard(false, "english/"
                + FileAccessor.ENGLISH_TEXT_MODEL_NAME, mFolderPath + "/"
                + FileAccessor.ENGLISH_TEXT_MODEL_NAME);
    }

    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(MyApplication.getInstance());
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(
                SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mFolderPath + "/"
                        + FileAccessor.TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(
                SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mFolderPath
                        + "/" + FileAccessor.SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        this.mSpeechSynthesizer.setParam(
                SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mFolderPath + "/"
                        + FileAccessor.LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("2114518");
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey("kFQZClsTBjCrloe9gKX4Gv1y",
                "heyGmjv1bGMniI5gCQwtmsqwepkkfspt");
        // 设置在线发音人参数（还可设置其他参数）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER,
                SpeechSynthesizer.SPEAKER_FEMALE);
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE,
                SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(可以不使用，只是验证授权是否成功)
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isSuccess()) {
            toPrint("auth success");
        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            toPrint("auth failed errorMsg=" + errorMsg);
        }
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        int result = mSpeechSynthesizer.loadEnglishModel(mFolderPath + "/"
                + FileAccessor.ENGLISH_TEXT_MODEL_NAME, mFolderPath + "/"
                + FileAccessor.ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        toPrint("loadEnglishModel result=" + result);
    }

    private void initParams() {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "0");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "0");
    }

    public void speak(String something) {
        stop();
        String text = "";
        if (!TextUtils.isEmpty(something))
            text = something;
        int result = this.mSpeechSynthesizer.speak(text);
        if (result < 0) {
            toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    private void pause() {
        this.mSpeechSynthesizer.pause();
    }

    private void resume() {
        this.mSpeechSynthesizer.resume();
    }

    private void stop() {
        this.mSpeechSynthesizer.stop();
    }

    public void synthesize(String something) {
        String text = "";
        if (!TextUtils.isEmpty(something))
            text = something;
        int result = this.mSpeechSynthesizer.synthesize(text);
        if (result < 0) {
            toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    public void batchSpeak() {
        List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
        bags.add(getSpeechSynthesizeBag("123456", "0"));
        bags.add(getSpeechSynthesizeBag("你好", "1"));
        bags.add(getSpeechSynthesizeBag("使用百度语音合成SDK", "2"));
        bags.add(getSpeechSynthesizeBag("hello", "3"));
        bags.add(getSpeechSynthesizeBag("这是一个demo工程", "4"));
        int result = this.mSpeechSynthesizer.batchSpeak(bags);
        if (result < 0) {
            toPrint("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    private SpeechSynthesizeBag getSpeechSynthesizeBag(String text,
            String utteranceId) {
        SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
        speechSynthesizeBag.setText(text);
        speechSynthesizeBag.setUtteranceId(utteranceId);
        return speechSynthesizeBag;
    }

    /*
     * @param arg0
     */
    @Override
    public void onSynthesizeStart(String utteranceId) {
        toPrint("onSynthesizeStart utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     * 
     * @param arg1
     * 
     * @param arg2
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data,
            int progress) {
        // toPrint("onSynthesizeDataArrived");
    }

    /*
     * @param arg0
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
        toPrint("onSynthesizeFinish utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     */
    @Override
    public void onSpeechStart(String utteranceId) {
        toPrint("onSpeechStart utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     * 
     * @param arg1
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        // toPrint("onSpeechProgressChanged");
    }

    /*
     * @param arg0
     */
    @Override
    public void onSpeechFinish(String utteranceId) {
        toPrint("onSpeechFinish utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     * 
     * @param arg1
     */
    @Override
    public void onError(String utteranceId, SpeechError error) {
        toPrint("onError error=" + "(" + error.code + ")" + error.description
                + "--utteranceId=" + utteranceId);
    }

    private void toPrint(String string) {
        Log.e("-->", string);
    }
}
