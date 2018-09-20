import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import SentQuery from './sentQuery'
import SentList from './sentList'

const SentMsg = ({
  dispatch,
  sentMsg,
}) => {
  return (
    <div>
      <SentQuery dispatch={dispatch} {...sentMsg} />
      <SentList dispatch={dispatch} {...sentMsg} />
    </div>
  )
}

SentMsg.propTypes = {
  dispatch: PropTypes.func.isRequired,
  sentMsg: PropTypes.obj,
  loading: PropTypes.isRequired,
}
export default connect(({ sentMsg }) => ({ sentMsg }))(SentMsg)
