package lk.exon.temco_loan_system.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "member_has_category")
@NamedQueries({
    @NamedQuery(name = "MemberHasCategory.findAll", query = "SELECT m FROM MemberHasCategory m"),
    @NamedQuery(name = "MemberHasCategory.findById", query = "SELECT m FROM MemberHasCategory m WHERE m.id = :id"),
    @NamedQuery(name = "MemberHasCategory.findByMemberId", query = "SELECT m FROM MemberHasCategory m WHERE m.member.id = :memberId"),
    @NamedQuery(name = "MemberHasCategory.findByCategoryId", query = "SELECT m FROM MemberHasCategory m WHERE m.memberCategory.id = :categoryId"),
    @NamedQuery(name = "MemberHasCategory.findByIsActive", query = "SELECT m FROM MemberHasCategory m WHERE m.isActive = :isActive"),
    @NamedQuery(name = "MemberHasCategory.findByIsPrimary", query = "SELECT m FROM MemberHasCategory m WHERE m.isPrimary = :isPrimary")
})
public class MemberHasCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Member1 member;

    @JoinColumn(name = "member_category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MemberCategory memberCategory;

    @Column(name = "assigned_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public MemberHasCategory() {
    }

    public MemberHasCategory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member1 getMember() {
        return member;
    }

    public void setMember(Member1 member) {
        this.member = member;
    }

    public MemberCategory getMemberCategory() {
        return memberCategory;
    }

    public void setMemberCategory(MemberCategory memberCategory) {
        this.memberCategory = memberCategory;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MemberHasCategory)) {
            return false;
        }
        MemberHasCategory other = (MemberHasCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.exon.temco_loan_system.entity.MemberHasCategory[ id=" + id + " ]";
    }
}
