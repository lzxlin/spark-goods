package bigdata.domain;

public class userR {
    private int userID;
    private int itemID;
    private double scores;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setScores(double scores) {
        this.scores = scores;
    }

    public int getUserID() {
        return userID;
    }

    public int getItemID() {
        return itemID;
    }

    public double getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "userR:"+userID+" "+itemID+" "+scores;
    }
}
