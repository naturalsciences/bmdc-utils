/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.util.Locale;

/**
 *
 * @author thomas
 */
public class LocalizedString {

    private String underlyingString;
    private final Locale language;

    public LocalizedString(String underlyingString, Locale language) {
        this.underlyingString = underlyingString;
        this.language = language;
    }

    public String getUnderlyingString() {
        return underlyingString;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getLanguageString() {
        return language.getLanguage();
    }

    public void setUnderlyingString(String underlyingString) {
        this.underlyingString = underlyingString;
    }

}
