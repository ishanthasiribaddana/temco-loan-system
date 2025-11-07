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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "transaction_type")
@NamedQueries({
    @NamedQuery(name = "TransactionType.findAll", query = "SELECT t FROM TransactionType t"),
    @NamedQuery(name = "TransactionType.findById", query = "SELECT t FROM TransactionType t WHERE t.id = :id"),
    @NamedQuery(name = "TransactionType.findByType", query = "SELECT t FROM TransactionType t WHERE t.type = :type")})
public class TransactionType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionTypeId")
    private Collection<BankStatement> bankStatementCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionTypeId")
    private Collection<GeneralJournalEntry> generalJournalEntryCollection;

    public TransactionType() {
    }

    public TransactionType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<BankStatement> getBankStatementCollection() {
        return bankStatementCollection;
    }

    public void setBankStatementCollection(Collection<BankStatement> bankStatementCollection) {
        this.bankStatementCollection = bankStatementCollection;
    }

    public Collection<GeneralJournalEntry> getGeneralJournalEntryCollection() {
        return generalJournalEntryCollection;
    }

    public void setGeneralJournalEntryCollection(Collection<GeneralJournalEntry> generalJournalEntryCollection) {
        this.generalJournalEntryCollection = generalJournalEntryCollection;
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
        if (!(object instanceof TransactionType)) {
            return false;
        }
        TransactionType other = (TransactionType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.exon.temco_loan_system.entity.TransactionType[ id=" + id + " ]";
    }
    
}
