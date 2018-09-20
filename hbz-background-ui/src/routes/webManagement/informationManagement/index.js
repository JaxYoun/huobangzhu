import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Tabs } from 'antd'
import PicAndArtList from './picAndArtList'
import PicAndArtQuery from './picAndArtQuery'

const TabPane = Tabs.TabPane
const InformationManagement = ({
  dispatch,
  informationManagement,
}) => {
  const {
      infoData,
      activeKeys,
      picAndArt,
      picAndArtModal,
    } = informationManagement
  const onChange = (activeKey) => {
    dispatch({ type: 'informationManagement/update', payload: { activeKeys: activeKey } })
    dispatch({ type: 'informationManagement/updatePicAndArt', payload: { fields: {} } })
    dispatch({ type: 'informationManagement/getinfoData', payload: { type: 'NewsType' } })
  }
  return (
    <div>
      <Tabs defaultActiveKey="1" onChange={onChange}>
        <TabPane tab="图文信息管理" key="1">
        {activeKeys === '1' && <PicAndArtQuery infoData={infoData} dispatch={dispatch} />}
          <PicAndArtList {...picAndArt} dispatch={dispatch} picAndArtModal={picAndArtModal} infoData={infoData} />
        </TabPane>
        <TabPane tab="站内信息管理" key="2">
         
        </TabPane>
      </Tabs>
    </div>
  )
}

InformationManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  informationManagement: PropTypes.obj,
}
export default connect(({ informationManagement }) => ({ informationManagement }))(InformationManagement)
