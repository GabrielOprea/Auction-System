package organisers;

/**
 * Abstract class describing an employee, extended by both the Administrator and
 * the Broker
 */
abstract public class Employee {

    //Fields describing the employee's name, years of experience and rating
    String name;
    int yearsOfExperience;
    double rating;

    /**
     * Protected default constructor
     */
    protected Employee() {
    }

    /**
     * Constructor for proper instantiation, with the specified name, years of experience
     * and rating
     * @param name the name of the employee
     * @param yearsOfExperience the number of years of experince
     * @param rating the ratting accorded by the previous clients
     */
    protected Employee(String name, int yearsOfExperience, double rating) {
        this.name = name;
        this.yearsOfExperience = yearsOfExperience;
        this.rating = rating;
    }

    /**
     * Sets the name of the employee
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the years of experience
     * @param yearsOfExperience the experience to be set
     */
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Sets the rating
     * @param rating the rating to be set
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Gets the name of the employee
     * @return the name of the current employee
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the years of experience of this employee
     * @return the years of experience
     */
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Gets the rating of this employee
     * @return the corresponding rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Converts the employee to string
     * @return a string representation of the fields
     */
    @Override
    public String toString() {
        return "Employee {" +
                "name='" + name + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", rating=" + rating +
                '}';
    }
}
