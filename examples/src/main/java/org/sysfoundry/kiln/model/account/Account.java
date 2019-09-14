package org.sysfoundry.kiln.model.account;


import lombok.Data;

@Data
public class Account {

    private String name;
    private String owner;
    private String ownerEmail;
    private String country;

    public Account(String name,String owner,String ownerEmail,String country){

        this.name = name;
        this.owner = owner;
        this.ownerEmail = ownerEmail;
        this.country = country;
    }

}
