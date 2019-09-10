package nil.s.fsdb;

public class ItemFilme {

    private String id;
    private String title;
    private String poster_path = "https://image.tmdb.org/t/p/original/";
    private String backdrop_path = "https://image.tmdb.org/t/p/original/";
    private String release_date;
    private int budget;
    private long revenue;
    private String overview;
    private int runtime;
    private String key; //used for videos
    private double vote;
    private String type;
    private String char_job;

    public ItemFilme() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path += poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path += backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChar_job() {
        return char_job;
    }

    public void setChar_job(String char_job) {
        this.char_job = char_job;
    }

    @Override
    public String toString() {
        return "ItemFilme{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", release_date='" + release_date + '\'' +
                ", budget=" + budget +
                ", revenue=" + revenue +
                ", overview='" + overview + '\'' +
                ", runtime=" + runtime +
                ", key='" + key + '\'' +
                ", vote=" + vote +
                ", type='" + type + '\'' +
                ", char_job='" + char_job + '\'' +
                '}';
    }
}
