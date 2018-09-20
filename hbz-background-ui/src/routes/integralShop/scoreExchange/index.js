import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import UpShelfList from './upShelfList'
import UpShelfQuery from './upShelfQuery'
import ExchangeList from './exchangeList'
import ExchangeQuery from './exchangeQuery'
import RecommonList from './recommonList'
import RecommonQuery from './recommonQuery'
const TabPane = Tabs.TabPane
const ScoreExchange = ({
  dispatch,
  scoreExchange,
}) => {
  const {
    activeKeys,
    upShelfModal,
    upShelf,
    exchange,
    exchangeModal,
    deliverModal,
    wareTypeData,
    transData,
    recommonModal,
    recommon,
    recommonModalUpdate,
    } = scoreExchange
  const onChange = (activeKey) => {
    dispatch({ type: 'scoreExchange/update', payload: { activeKeys: activeKey } })
    dispatch({ type: 'scoreExchange/updateupShelf', payload: { fields: {} } })
    dispatch({ type: 'scoreExchange/updateExchange', payload: { fields: {} } })
    dispatch({ type: 'scoreExchange/updateRecommon', payload: { fields: {} } })
    dispatch({ type: 'scoreExchange/getTreeData', payload: { } })
    dispatch({ type: 'scoreExchange/getBrandData', payload: { } })
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="上架商品管理" key="1">
          {activeKeys === '1' && <UpShelfQuery upShelfModal={upShelfModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
          <UpShelfList {...upShelf} dispatch={dispatch} upShelfModal={upShelfModal} wareTypeData={wareTypeData} recommonModal={recommonModal} />
        </TabPane>
        <TabPane tab="推荐商品管理" key="2">
          {activeKeys === '2' && <RecommonQuery dispatch={dispatch} wareTypeData={wareTypeData} />}
          <RecommonList dispatch={dispatch} {...recommon} recommonModalUpdate={recommonModalUpdate} wareTypeData={wareTypeData} />
        </TabPane>
        <TabPane tab="积分兑换记录" key="3">
          {activeKeys === '3' && <ExchangeQuery exchangeModal={exchangeModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
          <ExchangeList {...exchange}
            dispatch={dispatch}
            transData={transData}
            exchangeModal={exchangeModal}
            wareTypeData={wareTypeData}
            deliverModal={deliverModal}
          />
        </TabPane>
      </Tabs>
    </div>
  )
}

ScoreExchange.propTypes = {
  dispatch: PropTypes.func.isRequired,
  scoreExchange: PropTypes.obj,
  wareTypeData: PropTypes.array,
  exchange: PropTypes.obj,
  exchangeModal: PropTypes.obj,
  deliverModal: PropTypes.obj,
  transData: PropTypes.obj,
}
export default connect(({ scoreExchange }) => ({ scoreExchange }))(ScoreExchange)
