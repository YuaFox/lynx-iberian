package dev.yua.lynxiberian.models;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;

@Entity
@Table(name="buckets")
public class Bucket implements Serializable {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Bucket getBucket(String name) {
        return new Bucket(name);
    }

    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private final long id;

    @Column(unique = true, nullable = false)
    private String name;


    public Bucket(){
        this.id = 0;
    }

    public Bucket(String name){
        this.id = 0;
        this.name = name;
    }

    public Bucket(int id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
