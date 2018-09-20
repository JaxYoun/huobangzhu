import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import BannerList from './bannerList'
import BannerQuery from './bannerQuery'

const TabPane = Tabs.TabPane
const AdManagement = ({
  dispatch,
  adManagement,
}) => {
  const {
      locationData,
      skipTypeData,
      activeKeys,
      banner,
      bannerModal,
    } = adManagement
  const onChange = (activeKey) => {
    dispatch({ type: 'adManagement/update', payload: { activeKeys: activeKey } })
    dispatch({ type: 'adManagement/updateBanner', payload: { fields: {} } })
    dispatch({ type: 'adManagement/getlocationData', payload: { type: 'BannerLocation' } })
    dispatch({ type: 'adManagement/getskipTypeData', payload: { type: 'BannerSkipType' } })
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="Banner管理" key="1">
        {activeKeys === '1' && <BannerQuery locationData={locationData} skipTypeData={skipTypeData} dispatch={dispatch} />}
          <BannerList {...banner} dispatch={dispatch} bannerModal={bannerModal} locationData={locationData} skipTypeData={skipTypeData} />
        </TabPane>
      </Tabs>
    </div>
  )
}

AdManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  adManagement: PropTypes.obj,
}
export default connect(({ adManagement }) => ({ adManagement }))(AdManagement)
