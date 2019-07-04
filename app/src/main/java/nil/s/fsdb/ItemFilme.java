package nil.s.fsdb;

public class ItemFilme {

    private String title;
    private String poster_path;
    private String release_date;

    public ItemFilme(String title, String poster_path, String release_date) {
        this.title = title;
        this.poster_path = "https://image.tmdb.org/t/p/original/" + poster_path;
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }
}
