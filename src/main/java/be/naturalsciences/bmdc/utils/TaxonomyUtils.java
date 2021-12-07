/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thomas
 */
public class TaxonomyUtils {

    public static String[] splitSpecies(String speciesName) {
        return speciesName.split(" ");
    }

    /**
     * *
     * Return the author and date information from the species name as it is
     * written.
     *
     * @param speciesName
     * @return
     */
    public static String getExactAuthorAndDate(String speciesName) {
        String withoutFirstLetter = speciesName.substring(1, speciesName.length() - 1);

        Pattern p = Pattern.compile("[A-Z]+");

        Matcher matcher = p.matcher(withoutFirstLetter);
        Integer authorStartsAt = null;
        if (matcher.find()) {
            authorStartsAt = matcher.end();
        }
        if (authorStartsAt != null) {
            return speciesName.substring(authorStartsAt - 1, speciesName.length()).trim();

        } else { //no uppercase substring found. It still is possible that the author is not set to uppercase
            return null;
        }
    }

    /**
     * *
     * Return the author and date information from the species name. Correct it
     * if the parentheses are partial eg "Koellikerina fasciulata Péron &
     * Lesueur, 1810)" results in "(Péron & Lesueur, 1810)"
     *
     * @param speciesName
     * @return
     */
    public static String getAuthorAndDate(String speciesName) {
        String r = getExactAuthorAndDate(speciesName);
        if (r != null) {
            if (r.startsWith("(")) {
                if (r.endsWith(")")) {
                    return r;
                } else {
                    return r + ")";
                }
            } else { //!r.startsWith("(")
                if (r.endsWith(")")) {
                    return "(" + r;
                } else {
                    return r;
                }
            }
        } else {
            return null;
        }
    }

    public static boolean nameIsBinomial(String speciesName) {
        String author = getExactAuthorAndDate(speciesName);
        if (author != null) {
            speciesName = speciesName.replace(author, "");
        }
        return StringUtils.countMatches(speciesName.trim(), " ") == 1;
    }

    public static boolean nameIsTrinomial(String speciesName) {
        return StringUtils.countMatches(speciesName.trim(), " ") >= 2 && Character.isLowerCase(speciesName.split(" ")[2].charAt(0));
    }

    /**
     * *
     * From a trinomial name, retrieve the species. Return null if no species is
     * present
     *
     * @param subspeciesName
     * @return
     */
    public static String getSpeciesFromSubspecies(String subspeciesName) {
        if (nameIsTrinomial(subspeciesName)) {
            return subspeciesName.split(" ")[0] + " " + subspeciesName.split(" ")[1];
        } else {
            return null;
        }
    }

    /**
     * *
     * From a binomial or trinomial name, retrive the genus. Return null if no
     * genus can be present
     *
     * @param subspeciesName
     * @return
     */
    public static String getGenusFromSpecies(String subspeciesName) {
        if (nameIsBinomial(subspeciesName) || nameIsTrinomial(subspeciesName) || isHybrid(subspeciesName)) {
            return subspeciesName.split(" ")[0];
        } else {
            return null;
        }
    }

    public static boolean isHybrid(String speciesName) {
        return speciesName.contains(" x ") || speciesName.contains("×") || speciesName.contains(" X ");
    }
}
