package datatype;

@SuppressWarnings({"NewClassNamingConvention", "unused"})
public class Pair<FirstElem, SecondElem> {

    private FirstElem firstElem;
    private SecondElem secondElem;

    public Pair(FirstElem l, SecondElem r){
        this.firstElem = l;
        this.secondElem = r;
    }

    public FirstElem getFirst(){
        return firstElem;
    }

    public SecondElem getSecond(){
        return secondElem;
    }

    public void setFirst(FirstElem l){
        this.firstElem = l;
    }

    public void setSecond(SecondElem r){
        this.secondElem = r;
    }

}
