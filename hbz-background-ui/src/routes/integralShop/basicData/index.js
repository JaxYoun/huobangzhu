import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import ClassificationQuery from './classificationQuery'
import ClassificationList from './classificationList'
import BrandList from './brandList'
import BrandQuery from './brandQuery'
import GoodsList from './goodsList'
import GoodsQuery from './goodsQuery'

const TabPane = Tabs.TabPane
const BasicData = ({
  dispatch,
  basicData,
}) => {
  const {
    activeKeys,
    wareTypeData,
    classification,
    classificationModal,
    brand,
    brandModal,
    goods,
    goodsModal,
    goodsShelvesModal } = basicData
  const onChange = (activeKey) => {
    dispatch({ type: 'basicData/update', payload: { activeKeys: activeKey } })
    dispatch({ type: 'basicData/updateBrand', payload: { fields: {} } })
    dispatch({ type: 'basicData/updateGoods', payload: { fields: {} } })
    dispatch({ type: 'basicData/updateClassification', payload: { fields: {} } })
    if (activeKey === '3') {
      dispatch({ type: 'basicData/getTreeData', payload: { } })
      dispatch({ type: 'basicData/getBrandData', payload: { } })
    }
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="商品分类管理" key="1">
          {activeKeys === '1' && <ClassificationQuery classificationModal={classificationModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
          <ClassificationList {...classification} dispatch={dispatch} classificationModal={classificationModal} wareTypeData={wareTypeData} />
        </TabPane>
        <TabPane tab="商品品牌管理" key="2">
          {activeKeys === '2' && <BrandQuery brandModal={brandModal} dispatch={dispatch} />}
          <BrandList {...brand} brandModal={brandModal} dispatch={dispatch} />
        </TabPane>
        <TabPane tab="商品配置管理" key="3">
          {activeKeys === '3' && <GoodsQuery dispatch={dispatch} wareTypeData={wareTypeData} goodsModal={goodsModal} />}
          <GoodsList {...goods} dispatch={dispatch} wareTypeData={wareTypeData} goodsModal={goodsModal} goodsShelvesModal={goodsShelvesModal} />
        </TabPane>
      </Tabs>
    </div>
  )
}

BasicData.propTypes = {
  dispatch: PropTypes.func.isRequired,
  basicData: PropTypes.obj,
  wareTypeData: PropTypes.array,
}
export default connect(({ basicData }) => ({ basicData }))(BasicData)
