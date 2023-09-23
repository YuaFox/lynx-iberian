package dev.yua.lynxiberian.models;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.yua.lynxiberian.LynxiberianApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="buckets")
public class Bucket implements Serializable {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Bucket getBucket(String name) {
        return LynxiberianApplication.getBucket(name).orElse(new Bucket(name));
    }

    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    @Getter
    private final long id;

    @Column(unique = true, nullable = false)
    @Getter @Setter
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

    public boolean exists(){
        return this.id != 0;
    }
}
