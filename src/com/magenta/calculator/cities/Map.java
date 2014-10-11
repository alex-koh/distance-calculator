
package com.magenta.calculator.cities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cities" type="{http://www.magenta-technology.ru/DistanceCalculator}Cities"/>
 *         &lt;element name="distances" type="{http://www.magenta-technology.ru/DistanceCalculator}Distances"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cities",
    "distances"
})
@XmlRootElement(name = "map", namespace = "http://www.magenta-technology.ru/DistanceCalculator")
public class Map {

    @XmlElement(namespace = "http://www.magenta-technology.ru/DistanceCalculator", required = true)
    protected Cities cities;
    @XmlElement(namespace = "http://www.magenta-technology.ru/DistanceCalculator", required = true)
    protected Distances distances;

    /**
     * Gets the value of the cities property.
     * 
     * @return
     *     possible object is
     *     {@link Cities }
     *     
     */
    public Cities getCities() {
        return cities;
    }

    /**
     * Sets the value of the cities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cities }
     *     
     */
    public void setCities(Cities value) {
        this.cities = value;
    }

    /**
     * Gets the value of the distances property.
     * 
     * @return
     *     possible object is
     *     {@link Distances }
     *     
     */
    public Distances getDistances() {
        return distances;
    }

    /**
     * Sets the value of the distances property.
     * 
     * @param value
     *     allowed object is
     *     {@link Distances }
     *     
     */
    public void setDistances(Distances value) {
        this.distances = value;
    }

}
