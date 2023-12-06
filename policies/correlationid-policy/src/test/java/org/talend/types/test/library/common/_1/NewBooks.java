
package org.talend.types.test.library.common._1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter2;


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
 *         &lt;element name="ListDate" type="{http://types.talend.org/test/Library/Common/1.0}DateType"/>
 *         &lt;element name="Book" type="{http://types.talend.org/test/Library/Common/1.0}BookType" maxOccurs="unbounded"/>
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
    "listDate",
    "book"
})
@XmlRootElement(name = "newBooks")
public class NewBooks {

    @XmlElement(name = "ListDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    protected Date listDate;
    @XmlElement(name = "Book", required = true)
    protected List<BookType> book;

    /**
     * Gets the value of the listDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getListDate() {
        return listDate;
    }

    /**
     * Sets the value of the listDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListDate(Date value) {
        this.listDate = value;
    }

    /**
     * Gets the value of the book property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the book property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBook().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BookType }
     * 
     * 
     */
    public List<BookType> getBook() {
        if (book == null) {
            book = new ArrayList<BookType>();
        }
        return this.book;
    }

}
