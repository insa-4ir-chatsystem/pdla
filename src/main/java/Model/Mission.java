package Model ;

public class Mission {
    private int id; 
    private String Name ;
    private String date;             
    private String nature;          
    private String time;            
    private String state;  
    private String description ; // à voir comment la rajouter
    private User reclamant;  
    private User benevole;    
    private User valideur;     


    public Mission(int id,String date, String nature, String time, String state,
                   User reclamant, User benevole, User valideur, String Name) {
        this.id = id;
        this.Name = Name ;
        this.date = date;
        this.nature = nature;
        this.time = time;
        this.state = state;
        this.reclamant = reclamant;
        this.benevole = benevole;
        this.valideur = valideur;
    }

 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getReclamant() {
        return reclamant;
    }

    public void setReclamant(User reclamant) {
        this.reclamant = reclamant;
    }

    public User getBenevole() {
        return benevole;
    }

    public void setBenevole(User benevole) {
        this.benevole = benevole;
    }

    public User getValideur() {
        return valideur;
    }

    public void setValideur(User valideur) {
        this.valideur = valideur;
    }

    // Méthode pour afficher la mission
    @Override
    public String toString() {
        return "Mission {" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", nature='" + nature + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                ", reclamant=" + reclamant +
                ", benevole=" + benevole +
                ", valideur=" + valideur +
                '}';
    }
}
