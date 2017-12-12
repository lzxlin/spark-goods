package bigdata.domain;

import java.io.Serializable;

public class Follow implements Serializable{
    private String itemID;
    private double followValue;

    public String getItemID(){
        return itemID;
    }
    public double getFollowValue(){
        return followValue;
    }
    public void setItemID(String item){this.itemID=item;}
    public void setFollowValue(double follow){this.followValue=follow;}

    @Override
    public String toString() {
        return "Follow{" +
                "itemID=" + itemID +
                ", followValue=" + followValue +
                '}';
    }

}
