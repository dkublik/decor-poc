package pl.dk.decor.decorator;

public class PlayheadService {

    public int getLastWatchSec(int videoId) {
        return (int)(Math.random() * 50 + 10);
    }
}
