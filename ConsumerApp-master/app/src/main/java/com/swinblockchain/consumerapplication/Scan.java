package com.swinblockchain.consumerapplication;

import android.os.Parcel;
import android.os.Parcelable;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * Used to hold information about a Scan
 *
 * @author John Humphrys
 */
public class Scan implements Parcelable {

    String accAddr;
    String pubKey;
    String privKey;

    public Scan(Parcel in) {
        super();
        readFromParcel(in);
    }

    public Scan(String accAddr, String pubKey, String privKey) {
        this.accAddr = accAddr;
        this.pubKey = pubKey;
        this.privKey = privKey;
    }

    public static final Parcelable.Creator<Scan> CREATOR = new Parcelable.Creator<Scan>() {
        public Scan createFromParcel(Parcel in) {
            return new Scan(in);
        }

        public Scan[] newArray(int size) {

            return new Scan[size];
        }

    };

    public String getAccAddr() {
        return accAddr;
    }

    public void setAccAddr(String accAddr) {
        this.accAddr = accAddr;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.accAddr);
        parcel.writeString(this.pubKey);
        parcel.writeString(this.privKey);
    }

    public void readFromParcel(Parcel in) {
        accAddr = in.readString();
        pubKey = in.readString();
        privKey = in.readString();
    }
}