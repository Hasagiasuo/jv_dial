package org.example.dial.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "calls")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "caller_id")
    public User caller;

    @ManyToOne
    @JoinColumn(name = "callee_id")
    public User callee;

    public Date startAt;
    public Date endAt;
}
