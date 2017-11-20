package com.swinblockchain.consumerapplication;

import android.os.Parcel;
import android.os.Parcelable;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * The producer contains all information about a single producer
 *
 *  @author John Humphrys
 */
public class Producer implements Parcelable {
    String producerName;
    double producerTimestamp;
    String producerLocation;

    /**
     * Main constructor
     *
     * @param producerName
     * @param producerTimestamp
     * @param producerLocation
     */
    public Producer(String producerName, double producerTimestamp, String producerLocation) {
        this.producerName = producerName;
        this.producerTimestamp = producerTimestamp;
        this.producerLocation = producerLocation;
    }


    public Producer(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Producer> CREATOR = new Parcelable.Creator<Producer>() {
        public Producer createFromParcel(Parcel in) {
            return new Producer(in);
        }

        public Producer[] newArray(int size) {

            return new Producer[size];
        }
    };

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public double getProducerTimestamp() {
        return producerTimestamp;
    }

    public void setProducerTimestamp(double producerTimestamp) {
        this.producerTimestamp = producerTimestamp;
    }

    public String getProducerLocation() {
        return producerLocation;
    }

    public void setProducerLocation(String producerLocation) {
        this.producerLocation = producerLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.producerName);
        parcel.writeDouble(this.producerTimestamp);
        parcel.writeString(this.producerLocation);
    }

    public void readFromParcel(Parcel in) {
        producerName = in.readString();
        producerTimestamp = in.readDouble();
        producerLocation = in.readString();
    }
}
