package nil.s.fsdb;

public class ItemFilmePessoas {

    private String name;
    private String character;
    private String profile_path = "https://image.tmdb.org/t/p/original/";
    private String id;

    public ItemFilmePessoas(String name, String character, String profile_path, String id){

        this.name = name;
        this.character = character;
        this.profile_path += profile_path;
        this.id = id;
    }

    public ItemFilmePessoas(String id, String name, String profile_path){
        this.name = name;
        this.profile_path += profile_path;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getId() {
        return id;
    }
}
