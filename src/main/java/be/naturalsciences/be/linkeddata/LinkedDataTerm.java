/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.be.linkeddata;

/**
 *
 * @author thomas
 */
public class LinkedDataTerm {

    private String identifier;  //an identifier in an external vocabulary, i.e. the EARS ontology or the BODC Tool list L22 (can only be url)
    private String name;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedDataTerm(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }
}
