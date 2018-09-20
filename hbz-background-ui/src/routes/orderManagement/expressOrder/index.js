import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import QueryList from './queryList'
import QueryHistroy from './queryHistory'
import ExpressList from './expressList'
import HistoryList from './historyList'

const { TabPane } = Tabs
const ExpressOrder = ({
  expressOrder,
  dispatch,
}) => {
  const { activeKey,
    provinceFromData,
    cityFromData,
    countyFromData,
    provinceToData,
    cityToData,
    countyToData,
    selectData,
    commonData,
    modalState,
    payData,
    logisticsModal,
    addExpressModal,
    expressShowModal,
    fields,
    fieldsHistory,
    queryCollapse,
    historyCollapse,
    addExpressForEachModal,
   } = expressOrder
  const onChange = (activeKeys) => {
    dispatch({ type: 'expressOrder/update', payload: { fields: {}, sendfields: {}, activeKey: activeKeys } })
  }
  const queryListProps = {
    provinceFromData,
    cityFromData,
    countyFromData,
    provinceToData,
    cityToData,
    countyToData,
    selectData,
    queryCollapse,
    historyCollapse,
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="快递列表" key="1">
          {activeKey === '1' && <QueryList queryListProps={queryListProps} dispatch={dispatch} />}
          <ExpressList
            dispatch={dispatch}
            commonData={commonData}
            modalState={modalState}
            payData={payData}
            logisticsModal={logisticsModal}
            selectData={selectData}
            addExpressModal={addExpressModal}
            fields={fields}
          />
        </TabPane>
        <TabPane tab="快递派单记录" key="2">
          {activeKey === '2' && <QueryHistroy queryListProps={queryListProps} dispatch={dispatch} />}
          <HistoryList
            dispatch={dispatch}
            commonData={commonData}
            modalState={modalState}
            payData={payData}
            logisticsModal={logisticsModal}
            selectData={selectData}
            addExpressModal={addExpressModal}
            fieldsHistory={fieldsHistory}
            expressShowModal={expressShowModal}
            addExpressForEachModal={addExpressForEachModal}
          />
        </TabPane>
      </Tabs>
    </div>
  )
}

ExpressOrder.propTypes = {
  expressOrder: PropTypes.obj,
  dispatch: PropTypes.func.isRequired,
}
export default connect(({ expressOrder }) => ({ expressOrder }))(ExpressOrder)
