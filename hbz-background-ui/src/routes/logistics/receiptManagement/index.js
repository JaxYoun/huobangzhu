import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import ReceiptList from './receiptList'
import ReceiptQuery from './receiptQuery'

const ReceiptManagement = ({
  dispatch,
  receiptManagement,
}) => {
  return (
    <div>
      <ReceiptQuery dispatch={dispatch} {...receiptManagement} />
      <ReceiptList dispatch={dispatch} {...receiptManagement} />
    </div>
  )
}

ReceiptManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  receiptManagement: PropTypes.obj,
}
export default connect(({ receiptManagement }) => ({ receiptManagement }))(ReceiptManagement)
