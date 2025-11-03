/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lk.exon.temco_loan_system.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "general_journal_entry")
@NamedQueries({
    @NamedQuery(name = "GeneralJournalEntry.findAll", query = "SELECT g FROM GeneralJournalEntry g"),
    @NamedQuery(name = "GeneralJournalEntry.findById", query = "SELECT g FROM GeneralJournalEntry g WHERE g.id = :id"),
    @NamedQuery(name = "GeneralJournalEntry.findByDescription", query = "SELECT g FROM GeneralJournalEntry g WHERE g.description = :description"),
    @NamedQuery(name = "GeneralJournalEntry.findByAmount", query = "SELECT g FROM GeneralJournalEntry g WHERE g.amount = :amount"),
    @NamedQuery(name = "GeneralJournalEntry.findByDate", query = "SELECT g FROM GeneralJournalEntry g WHERE g.date = :date"),
    @NamedQuery(name = "GeneralJournalEntry.findByIsActive", query = "SELECT g FROM GeneralJournalEntry g WHERE g.isActive = :isActive"),
    @NamedQuery(name = "GeneralJournalEntry.findByChequeNo", query = "SELECT g FROM GeneralJournalEntry g WHERE g.chequeNo = :chequeNo")})
public class GeneralJournalEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "amount")
    private double amount;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "cheque_no")
    private String chequeNo;
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TransactionType transactionTypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalJournalEntrySub")
    private Collection<GeneralJournalEntryManager> generalJournalEntryManagerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalJournalEntryIdMain")
    private Collection<GeneralJournalEntryManager> generalJournalEntryManagerCollection1;

    public GeneralJournalEntry() {
    }

    public GeneralJournalEntry(Integer id) {
        this.id = id;
    }

    public GeneralJournalEntry(Integer id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public TransactionType getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(TransactionType transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Collection<GeneralJournalEntryManager> getGeneralJournalEntryManagerCollection() {
        return generalJournalEntryManagerCollection;
    }

    public void setGeneralJournalEntryManagerCollection(Collection<GeneralJournalEntryManager> generalJournalEntryManagerCollection) {
        this.generalJournalEntryManagerCollection = generalJournalEntryManagerCollection;
    }

    public Collection<GeneralJournalEntryManager> getGeneralJournalEntryManagerCollection1() {
        return generalJournalEntryManagerCollection1;
    }

    public void setGeneralJournalEntryManagerCollection1(Collection<GeneralJournalEntryManager> generalJournalEntryManagerCollection1) {
        this.generalJournalEntryManagerCollection1 = generalJournalEntryManagerCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalEntry)) {
            return false;
        }
        GeneralJournalEntry other = (GeneralJournalEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.exon.temco_loan_system.entity.GeneralJournalEntry[ id=" + id + " ]";
    }
    
}
