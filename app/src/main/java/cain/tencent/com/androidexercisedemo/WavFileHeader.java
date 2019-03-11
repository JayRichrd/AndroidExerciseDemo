package cain.tencent.com.androidexercisedemo;

public class WavFileHeader {

    public static final int WAV_FILE_HEADER_SIZE = 44;
    public static final int WAV_CHUNKSIZE_EXCLUDE_DATA = 36;

    public static final int WAV_CHUNKSIZE_OFFSET = 4;
    public static final int WAV_SUB_CHUNKSIZE1_OFFSET = 16;
    public static final int WAV_SUB_CHUNKSIZE2_OFFSET = 40;

    public String mChunkID = "RIFF";
    public int mChunkSize = 0;
    public String mFormat = "WAVE";

    public String mSubChunk1ID = "fmt ";
    public int mSubChunk1Size = 16;
    public short mAudioFormat = 1;
    public short mNumChannel = 1;
    public int mSampleRate = 8000;
    public int mByteRate = 0;
    public short mBlockAlign = 0;
    public short mBitsPerSample = 8;

    public String mSubChunk2ID = "data";
    public int mSubChunk2Size = 0;

    public WavFileHeader() {

    }

    public WavFileHeader(int sampleRateInHz, int channels, int bitsPerSample) {
        mSampleRate = sampleRateInHz;
        mBitsPerSample = (short) bitsPerSample;
        mNumChannel = (short) channels;
        mByteRate = mSampleRate * mNumChannel * mBitsPerSample / 8;
        mBlockAlign = (short) (mNumChannel * mBitsPerSample / 8);
    }

    public String getmChunkID() {
        return mChunkID;
    }

    public void setmChunkID(String mChunkID) {
        this.mChunkID = mChunkID;
    }

    public int getmChunkSize() {
        return mChunkSize;
    }

    public void setmChunkSize(int mChunkSize) {
        this.mChunkSize = mChunkSize;
    }

    public String getmFormat() {
        return mFormat;
    }

    public void setmFormat(String mFormat) {
        this.mFormat = mFormat;
    }

    public String getmSubChunk1ID() {
        return mSubChunk1ID;
    }

    public void setmSubChunk1ID(String mSubChunk1ID) {
        this.mSubChunk1ID = mSubChunk1ID;
    }

    public int getmSubChunk1Size() {
        return mSubChunk1Size;
    }

    public void setmSubChunk1Size(int mSubChunk1Size) {
        this.mSubChunk1Size = mSubChunk1Size;
    }

    public short getmAudioFormat() {
        return mAudioFormat;
    }

    public void setmAudioFormat(short mAudioFormat) {
        this.mAudioFormat = mAudioFormat;
    }

    public short getmNumChannel() {
        return mNumChannel;
    }

    public void setmNumChannel(short mNumChannel) {
        this.mNumChannel = mNumChannel;
    }

    public int getmSampleRate() {
        return mSampleRate;
    }

    public void setmSampleRate(int mSampleRate) {
        this.mSampleRate = mSampleRate;
    }

    public int getmByteRate() {
        return mByteRate;
    }

    public void setmByteRate(int mByteRate) {
        this.mByteRate = mByteRate;
    }

    public short getmBlockAlign() {
        return mBlockAlign;
    }

    public void setmBlockAlign(short mBlockAlign) {
        this.mBlockAlign = mBlockAlign;
    }

    public short getmBitsPerSample() {
        return mBitsPerSample;
    }

    public void setmBitsPerSample(short mBitsPerSample) {
        this.mBitsPerSample = mBitsPerSample;
    }

    public String getmSubChunk2ID() {
        return mSubChunk2ID;
    }

    public void setmSubChunk2ID(String mSubChunk2ID) {
        this.mSubChunk2ID = mSubChunk2ID;
    }

    public int getmSubChunk2Size() {
        return mSubChunk2Size;
    }

    public void setmSubChunk2Size(int mSubChunk2Size) {
        this.mSubChunk2Size = mSubChunk2Size;
    }
}
