import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import InfoList from './infoList'
import InfoQuery from './infoQuery'


const StorageInfo = ({
  dispatch,
  storageInfo,
}) => {
  return (
    <div>
      <InfoQuery dispatch={dispatch} {...storageInfo} />
      <InfoList dispatch={dispatch} {...storageInfo} />
    </div>
  )
}

StorageInfo.propTypes = {
  dispatch: PropTypes.func.isRequired,
  storageInfo: PropTypes.obj,
}
export default connect(({ storageInfo }) => ({ storageInfo }))(StorageInfo)
