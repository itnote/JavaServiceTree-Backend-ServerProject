package egovframework.api.rivalWar.compareItem.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import egovframework.api.rivalWar.menu.vo.MenuDTO;
import egovframework.api.rivalWar.specHashTag.vo.SpecHashTagDTO;
import egovframework.com.ext.jstree.springHibernate.core.vo.JsTreeHibernateSearchDTO;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-08.
 */
@Entity
@Table(name = "T_JSTREE_COMPARE_ITEM")
@SelectBeforeUpdate(value = true)
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "JsTreeSequence", sequenceName = "S_JSTREE_COMPARE_ITEM", allocationSize = 1)
public class CompareItemDTO extends JsTreeHibernateSearchDTO implements Serializable {

    private static final long serialVersionUID = 4641920581390357882L;

    public CompareItemDTO() {
        super();
    }

    public CompareItemDTO(Boolean copyBooleanValue) {
        super();
        this.copyBooleanValue = copyBooleanValue;
    }

    /*
	 * Extend Bean Field
	 */
    private Boolean copyBooleanValue;

    @Transient
    public Boolean getCopyBooleanValue() {
        copyBooleanValue = false;
        if (this.getCopy() == 0) {
            copyBooleanValue = false;
        } else {
            copyBooleanValue = true;
        }
        return copyBooleanValue;
    }

    public void setCopyBooleanValue(Boolean copyBooleanValue) {
        this.copyBooleanValue = copyBooleanValue;
    }

    //private Number mappingMenuId;
    private String compareItemName;

    @Column(name = "COMPARE_ITEM_NAME")
    public String getCompareItemName() {
        return compareItemName;
    }

    public void setCompareItemName(String compareItemName) {
        this.compareItemName = compareItemName;
    }

    @Override
    public <T extends JsTreeHibernateSearchDTO> void setFieldFromNewInstance(T paramInstance) {
        if (paramInstance instanceof CompareItemDTO) {

        }
    }

    private MenuDTO menuDTO;

    //영속성 전이 설정
    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)   //Lazy Loading 설정
    @JoinColumn(name="MAPPING_MENU_ID") //조인 컬럼 설정
    public MenuDTO getMenuDTO() {
        return menuDTO;
    }

    public void setMenuDTO(MenuDTO menuDTO) {
        this.menuDTO = menuDTO;
    }

    private List<SpecHashTagDTO> specHashTagDTOs = new ArrayList<SpecHashTagDTO>();

    // default fetch type = LAZY
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    @OneToMany(mappedBy = "compareItemDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<SpecHashTagDTO> getSpecHashTagDTOs() {
        return specHashTagDTOs;
    }

    public void setSpecHashTagDTOs(List<SpecHashTagDTO> specHashTagDTOs) {
        this.specHashTagDTOs = specHashTagDTOs;
    }
}