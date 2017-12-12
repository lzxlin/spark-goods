package bigdata.domain;

import java.io.Serializable;

public class rawData implements Serializable{
    private int userID;
    private int itemID;
    private int browser_num;
    private float stay_time;
    private int collect;
    private int buy_num;
    private double scores;

    public int getUserID(){
        return userID;
    }
    public int getItemID(){
        return itemID;
    }
    public int getBrowser_num(){
        return browser_num;
    }
    public float getStay_time(){
        return stay_time;
    }
    public int getCollect(){
        return collect;
    }
    public int getBuy_num(){
        return buy_num;
    }
    public double getScores(){
        return scores;
    }
    public void setUserID(int userID){this.userID=userID;}
    public void setItemID(int itemID){this.itemID=itemID;}
    public void setBrowser_num(int num){this.browser_num=num;}
    public void setStay_time(float time){this.stay_time=time;}
    public void setCollect(int collect){this.collect=collect;}
    public void setBuy_num(int num){this.buy_num=num;}
    public void setScores(double scores){this.scores=scores;}

    @Override
    public String toString() {
        return "rawData:"+userID+" "+itemID+" "+browser_num+" "+stay_time+" "+collect+" "+buy_num+" "+scores;
    }

}
