package nil.s.fsdb;

public class ItemFilmografia {

    private String year;
    private String type;
    private String name;
    private String character;
    private String job;
    private String imagePath = "https://image.tmdb.org/t/p/original/";
    private String id;

    public ItemFilmografia() {
    }

    public ItemFilmografia(String year, String type, String name, String character, String job, String imagePath) {
        this.year = year;
        this.type = type;
        this.name = name;
        this.character = character;
        this.job = job;
        this.imagePath += imagePath;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath += imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ItemFilmografia{" +
                "year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", character='" + character + '\'' +
                ", job='" + job + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
