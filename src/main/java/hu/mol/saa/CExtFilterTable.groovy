package hu.mol.saa

import org.asi.ui.extfilteringtable.ExtFilterGenerator
import org.asi.ui.extfilteringtable.ExtFilterTable

/**
 * Created by tamasturcsek on 2015. 10. 03..
 */
class CExtFilterTable extends ExtFilterTable {

    public CExtFilterTable(def container) {
        setContainerDataSource(container)
        setSelectable(true)
        setMultiSelect(true)
        setFilterBarVisible(true)
        setImmediate(true)
        setSizeFull()
    }
}
