public class Music {
    private int id;
    private String title;
    private String artist;
    private double duration; // em minutos

    public Music(int id, String title, String artist, double duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public double getDuration() { return duration; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%.2f min)", id, title, artist, duration);
    }
}
