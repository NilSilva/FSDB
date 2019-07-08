package nil.s.fsdb;

public class ItemFilme {

    private String id;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private int budget;
    private long revenue;
    private String overview;
    private int runtime;
    private String key;

    public ItemFilme(String title, String poster_path, String backdrop_path, String overview, String key) {
        this.title = title;
        this.poster_path = "https://image.tmdb.org/t/p/original/" + poster_path;
        this.backdrop_path = "https://image.tmdb.org/t/p/original/" + backdrop_path;
        this.overview = overview;
        this.key = key;
    }

    public ItemFilme(String id, String title, String poster_path, String release_date) {
        this.id = id;
        this.title = title;
        this.poster_path = "https://image.tmdb.org/t/p/original/" + poster_path;
        this.release_date = release_date;
    }

    public ItemFilme(String title, String poster_path, String backdrop_path, String release_date, int budget, long revenue, String overview, int runtime, String key){
        this.id = id;
        this.title = title;
        this.poster_path = "https://image.tmdb.org/t/p/original/" + poster_path;
        this.backdrop_path = "https://image.tmdb.org/t/p/original/" + backdrop_path;
        this.release_date = release_date;
        this.budget = budget;
        this.revenue = revenue;
        this.overview = overview;
        this.runtime = runtime;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public String getOverview() {
        return overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getKey() {
        return key;
    }
}
