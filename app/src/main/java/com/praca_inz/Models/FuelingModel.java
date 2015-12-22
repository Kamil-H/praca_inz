package com.praca_inz.Models;

/**
 * Created by KamilH on 2015-10-18.
 */
public class FuelingModel {
    private int id;
    private int day;
    private int month;
    private int year;

    private int monthYear;
    private float price, litres, cost;

    private String date;

    @Override
    public String toString() {
        return "FuelingModel{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", monthYear=" + monthYear +
                ", price=" + price +
                ", litres=" + litres +
                ", cost=" + cost +
                ", date='" + date + '\'' +
                '}';
    }

    public FuelingModel(int monthYear){
        String monthYearString = String.valueOf(monthYear);
        this.monthYear = monthYear;
        this.year = Integer.valueOf(monthYearString.substring(monthYearString.length()-4));
        this.month = Integer.valueOf(monthYearString.substring(0, monthYearString.length()-4));
    }

    public FuelingModel(float price, float litres, float cost, String date){
        String dateArr[] = date.split(" ");

        this.price = price;
        this.litres = litres;
        this.cost = cost;
        this.day = Integer.valueOf(dateArr[0]);
        this.month = Integer.valueOf(dateArr[1]);
        this.year = Integer.valueOf(dateArr[2]);
        this.date = date;
        this.monthYear = Integer.parseInt(month +""+ year);
    }

    public FuelingModel() {};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String dateArr[] = date.split(" ");
        this.date = date;
        this.day = Integer.valueOf(dateArr[0]);
        this.month = Integer.valueOf(dateArr[1]);
        this.year = Integer.valueOf(dateArr[2]);
        this.monthYear = Integer.parseInt(month + "" + year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getLitres() {
        return litres;
    }

    public void setLitres(float litres) {
        this.litres = litres;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(int monthYear) {
        this.monthYear = monthYear;
    }


}
