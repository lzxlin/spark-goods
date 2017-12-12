package bigdata.domain;

public class eval {
    private int rank;
    private int iterations;
    private double lambda;
    private double rmse;
    private double time;

    public void setRank(int rank){
        this.rank=rank;
    }
    public void setIterations(int iterations){
        this.iterations=iterations;
    }
    public void setLambda(double lambda){
        this.lambda=lambda;
    }
    public void setRmse(double rmse){
        this.rmse=rmse;
    }
    public void setTime(double time){
        this.time=time;
    }
    public int getRank(){return rank;}
    public int getIterations(){
        return iterations;
    }
    public double getLambda(){return lambda;}
    public double getRmse(){return rmse;}
    public double getTime() {return time;}

    @Override
    public String toString() {
        return "eval:"+rank+" "+iterations+" "+lambda+" "+rmse+" "+time;
    }

}
