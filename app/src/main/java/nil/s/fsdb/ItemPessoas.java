package nil.s.fsdb;

public class ItemPessoas {

    private String name;
    private String character;
    private String profile_path = "https://image.tmdb.org/t/p/original/";
    private String id;

    public ItemPessoas(){
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

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path += profile_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ItemPessoas{" +
                "name='" + name + '\'' +
                ", character='" + character + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
