package lk.exon.temco_loan_system.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "member_category")
@NamedQueries({
    @NamedQuery(name = "MemberCategory.findAll", query = "SELECT m FROM MemberCategory m"),
    @NamedQuery(name = "MemberCategory.findById", query = "SELECT m FROM MemberCategory m WHERE m.id = :id"),
    @NamedQuery(name = "MemberCategory.findByName", query = "SELECT m FROM MemberCategory m WHERE m.name = :name"),
    @NamedQuery(name = "MemberCategory.findByCode", query = "SELECT m FROM MemberCategory m WHERE m.code = :code"),
    @NamedQuery(name = "MemberCategory.findByIsActive", query = "SELECT m FROM MemberCategory m WHERE m.isActive = :isActive")
})
public class MemberCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberCategory")
    private Collection<MemberHasCategory> memberHasCategoryCollection;

    public MemberCategory() {
    }

    public MemberCategory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Collection<MemberHasCategory> getMemberHasCategoryCollection() {
        return memberHasCategoryCollection;
    }

    public void setMemberHasCategoryCollection(Collection<MemberHasCategory> memberHasCategoryCollection) {
        this.memberHasCategoryCollection = memberHasCategoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MemberCategory)) {
            return false;
        }
        MemberCategory other = (MemberCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.exon.temco_loan_system.entity.MemberCategory[ id=" + id + " ]";
    }
}
