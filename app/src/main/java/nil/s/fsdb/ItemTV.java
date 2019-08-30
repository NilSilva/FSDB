package nil.s.fsdb;

public class ItemTV {

    private String id;
    private String nome;
    private String backdrop_path = "https://image.tmdb.org/t/p/original/";
    private String poster_path = "https://image.tmdb.org/t/p/original/";
    private String overview;
    private String runtime;
    private String first_air_date;
    private boolean in_production;
    private String numeroEpisodios;
    private String numeroTemporadas;

    public ItemTV() {
    }

    public ItemTV(String id, String nome, String backdrop_path, String poster_path, String overview,
                  String runtime, String first_air_date, boolean in_production,
                  String numeroEpisodios, String numeroTemporadas) {
        this.id = id;
        this.nome = nome;
        this.backdrop_path += backdrop_path;
        this.poster_path += poster_path;
        this.overview = overview;
        this.runtime = runtime;
        this.first_air_date = first_air_date;
        this.in_production = in_production;
        this.numeroEpisodios = numeroEpisodios;
        this.numeroTemporadas = numeroTemporadas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path += backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path += poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public boolean getIn_production() {
        return in_production;
    }

    public void setIn_production(boolean in_production) {
        this.in_production = in_production;
    }

    public String getNumeroEpisodios() {
        return numeroEpisodios;
    }

    public void setNumeroEpisodios(String numeroEpisodios) {
        this.numeroEpisodios = numeroEpisodios;
    }

    public String getNumeroTemporadas() {
        return numeroTemporadas;
    }

    public void setNumeroTemporadas(String numeroTemporadas) {
        this.numeroTemporadas = numeroTemporadas;
    }

    @Override
    public String toString() {
        return "ItemTV{" +
                "id='" + id + '\'' +
                ",\n nome='" + nome + '\'' +
                ",\n backdrop_path='" + backdrop_path + '\'' +
                ",\n poster_path='" + poster_path + '\'' +
                ",\n overview='" + overview + '\'' +
                ",\n runtime='" + runtime + '\'' +
                ",\n first_air_date='" + first_air_date + '\'' +
                ",\n in_production='" + in_production + '\'' +
                ",\n numeroEpisodios='" + numeroEpisodios + '\'' +
                ",\n numeroTemporadas='" + numeroTemporadas + '\'' +
                '}';
    }
}
