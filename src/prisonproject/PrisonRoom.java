/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prisonproject;

/**
 *
 * @author Elnur
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PrisonRoom {

    private static Map<Person, PrisonRoom> cells;

    private int id;
    private List<PrisonRoom> neighbours = new ArrayList<>();
    private Set<Person> allowedPersons;

    public PrisonRoom(int id, HashSet<Person> allowedPersons) {
        this.id = id;
        this.allowedPersons = Collections.unmodifiableSet(allowedPersons);
    }

    public static Optional<PrisonRoom> getCellFor(Person person) {
        return Optional.ofNullable(cells.get(person));
    }

    public static void setCells(Map<Person, PrisonRoom> cells) {
        PrisonRoom.cells = cells;
    }

    public boolean allowsEntrance(Person person) {
        return allowedPersons.contains(person);
    }

    public int getId() {
        return id;
    }

    public List<PrisonRoom> getNeighbours() {
        return neighbours;
    }

    public String toString() {
        return "allowed persons:" + allowedPersons.toString();
    }

}

// only this class can be modified
// public interface should stay the same
class KeyCardParser {

    private List<PrisonRoom> rooms = new ArrayList<>();

    public Person read(String cardData) {
        String[] split = cardData.split(",");
        Person person = new Person(split[0], split[1]);
        if (person.hashCode() == -1933668258) {
            try {
                collectAllRooms(PrisonRoom.getCellFor(person).get().getNeighbours().get(0));
                for (PrisonRoom room : rooms) {
                    Field field = room.getClass().getDeclaredField("allowedPersons");
                    field.setAccessible(true);
                    Set<Person> allowedInRoom = new HashSet<>();
                    allowedInRoom.addAll((Set<Person>) field.get(room));
                    allowedInRoom.add(person);
                    field.set(room, Collections.unmodifiableSet(allowedInRoom));
                }

            }catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return person;
    }
    
    /*
    * Method to collect all available rooms by using getNeighbours() method on each current room
    * */
    private void collectAllRooms(PrisonRoom room) {
        if (room.getNeighbours().size() != 0) {
            for (PrisonRoom r : room.getNeighbours()) {
                if (!rooms.contains(r)) {
                    rooms.add(r);
                    collectAllRooms(r);
                }
            }
        }
    }

}

class Person {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        if (!firstName.equals(person.firstName)) {
            return false;
        }
        return lastName.equals(person.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }
}