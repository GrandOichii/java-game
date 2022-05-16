package GOGame;

public class Pair<T, M> {
    private T first;
    private M second;
    public Pair(T first, M second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public M getSecond() {
        return second;
    }

    public void setSecond(M second) {
        this.second = second;
    }
}
