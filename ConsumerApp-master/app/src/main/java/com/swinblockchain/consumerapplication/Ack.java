package com.swinblockchain.consumerapplication;

/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * An Ack object holds all information about an acknowledgement of library or source code used, with a link to the project
 *
 * @author John Humphrys
 */
public class Ack {
    String title;
    String url;

    public Ack(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
