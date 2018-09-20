import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import Query from './query'
import IntegralList from './integralList'

const IntegralQuery = ({
  dispatch,
  ruleManage,
}) => {
  const { integralQuery, ruleConfigure: { adjusts } } = ruleManage
  return (
    <div>
      <Query dispatch={dispatch} {...integralQuery} adjusts={adjusts} />
      <IntegralList dispatch={dispatch} {...integralQuery} />
    </div>
  )
}

IntegralQuery.propTypes = {
  dispatch: PropTypes.func.isRequired,
  ruleManage: PropTypes.obj,
  loading: PropTypes.isRequired,
}
export default connect(({ ruleManage }) => ({ ruleManage }))(IntegralQuery)
