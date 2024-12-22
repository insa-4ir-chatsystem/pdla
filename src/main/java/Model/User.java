package Model ;

public class User {
	private int ID ;
	private String FirstName ;
	private String LastName ;
	private int Age ;
	private String role ;
	private String password ;
	private String email ;
	
	public User(String FirstName, String LastName, int Age, String fonction, String pw, String email) {
		this.FirstName = FirstName ;
		this.LastName = LastName ;
		this.Age = Age ;
		this.role = fonction ;
		this.password = pw ;
		this.email = email ;
	}

	public void setID(int ID) {
		this.ID= ID ;
	}
	
	public void setFirstName(String firstName) {
        this.FirstName = firstName;       
    }
	
	public void setLastName(String lastName) {
        this.LastName = lastName;        
    }
    
    public void setAge(int age) {
        this.Age = age;        
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
	public void setPassword(String pw) {
		this.password = pw ;
	}
	
	public void setEmail(String email) {
		this.email = email ;		
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getFirstName() {
		return this.FirstName ;
	}
	
	public String getLastName() {
		return this.LastName ;
	}
	
	public int getID() {
		return this.ID ;
	}
	
	public int getAge() {
		return this.Age ;
	}
	
	public void getUser() {
		System.out.println(getID() + "\t" + getFirstName() + "\t" + getLastName() + "\t" + getAge()+ "\t" + getEmail()+ "\t" + getPassword()+ "\t" + getRole()) ;
	}


	public String getRole() {
		return this.role ;
	}

	public String getEmail() {
		return this.email ;
	}
	
	// pour un utilisateur "vierge"
	public void setNull() {
		this.FirstName = "";
		this.LastName = "" ;
		this.Age = -1 ;
		this.role = "" ;
		this.password = "" ;
		this.email = "" ;
	}


}