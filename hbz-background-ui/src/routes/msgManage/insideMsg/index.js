import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import InsideQuery from './insideQuery'
import InsideList from './insideList'

const InsideMsg = ({
  dispatch,
  insideMsg,
  loading,
}) => {
  return (
    <div>
      <InsideQuery dispatch={dispatch} {...insideMsg} />
      <InsideList dispatch={dispatch} {...insideMsg} loading={loading} />
    </div>
  )
}

InsideMsg.propTypes = {
  dispatch: PropTypes.func.isRequired,
  insideMsg: PropTypes.obj,
  loading: PropTypes.isRequired,
}
export default connect(({ insideMsg, loading }) => ({ insideMsg, loading }))(InsideMsg)
