/*
 * Copyright (c) $2017 CMPUT 301 University of Alberta. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.cqtran_sizebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Data{
    private String name;
    private String date;
    private Double neck;
    private Double bust;
    private Double chest;
    private Double waist;
    private Double hip;
    private Double inseam;
    private String comment;

    public Data(String name, String date, Double neck, Double bust, Double chest, Double waist,
                       Double hip, Double inseam, String comment) {
        this.name = name;
        this.date = date;
        this.neck = neck;
        this.bust = bust;
        this.chest = chest;
        this.waist = waist;
        this.hip = hip;
        this.inseam = inseam;
        this.comment = comment;
    }


    public String getName() {
        return name;
    }
    public String getDate() { return date.toString();}
    public String getNeck() {
        return neck.toString();
    }
    public String getBust() {
        return bust.toString();
    }
    public String getChest() {
        return chest.toString();
    }
    public String getWaist() {
        return waist.toString();
    }
    public String getHip() {
        return hip.toString();
    }
    public String getInseam() {
        return inseam.toString();
    }
    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        // because not alot of space only name, bust, chest, waist, inseam values are shown
        return "Name: " + name + " Bust: " + (bust) +
                " Chest: " + (chest) + " Waist: "+ (waist) + " Inseam: " + (inseam);
    }
}



