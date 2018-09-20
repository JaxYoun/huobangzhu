import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import QueryBuy from './queryBuy'
import BuyList from './buyList'
import SendList from './sendList'
import QuerySend from './querySend'

const TabPane = Tabs.TabPane
const CityDistribution = ({
  cityDistribution,
  dispatch,
}) => {
  const { fields,
     tableTime,
     provinceData,
     countyData,
     cityData,
     modalState,
     selectData,
     payData,
     teakUserData,
     logisticsModal,
     platformData,
     buyData,
     sendData,
     provinceDataSend,
     cityDataSend,
     countyDataSend,
     sendfields,
     modalStateSend,
     activeKey,
     buyCollapse,
     sendCollapse,
     } = cityDistribution
  const queryLineBuyProps = {
    provinceData,
    cityData,
    countyData,
    selectData,
    buyCollapse,
  }
  const queryLineSendProps = {
    provinceDataSend,
    cityDataSend,
    countyDataSend,
    selectData,
    sendCollapse,
  }
  const onChange = (activeKeys) => {
    dispatch({ type: 'cityDistribution/update', payload: { fields: {}, sendfields: {}, activeKey: activeKeys } })
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="帮我买订单" key="1">
          {activeKey === '1' && <QueryBuy queryLineProps={queryLineBuyProps} dispatch={dispatch} />}
          <BuyList fields={fields}
            tableTime={tableTime}
            dispatch={dispatch}
            modalState={modalState}
            payData={payData}
            teakUserData={teakUserData}
            logisticsModal={logisticsModal}
            buyData={buyData}
            platformData={platformData}
          />
        </TabPane>
        <TabPane tab="帮我送订单" key="2">
        {activeKey === '2' && <QuerySend queryLineProps={queryLineSendProps} dispatch={dispatch} />}
          <SendList
            fields={sendfields}
            tableTime={tableTime}
            dispatch={dispatch}
            modalStateSend={modalStateSend}
            payData={payData}
            teakUserData={teakUserData}
            logisticsModal={logisticsModal}
            sendData={sendData}
            platformData={platformData}
          />
        </TabPane>
      </Tabs>
    </div>
  )
}

CityDistribution.propTypes = {
  cityDistribution: PropTypes.obj,
  dispatch: PropTypes.func.isRequired,
  modalState: PropTypes.obj,
  selectData: PropTypes.obj,
  collectionData: PropTypes.obj,
  logisticsModal: PropTypes.obj,
  buyData: PropTypes.obj,
  platformData: PropTypes.obj,
  sendData: PropTypes.obj,
  modalStateSend: PropTypes.obj,
}
export default connect(({ cityDistribution }) => ({ cityDistribution }))(CityDistribution)
