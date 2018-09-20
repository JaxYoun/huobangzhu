import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import Query from './query'
import ReputationList from './reputationList'

const IntegralQuery = ({
  dispatch,
  ruleManage,
}) => {
  const { reputationQuery, ruleConfigure: { adjusts } } = ruleManage
  return (
    <div>
      <Query dispatch={dispatch} {...reputationQuery} adjusts={adjusts} />
      <ReputationList dispatch={dispatch} {...reputationQuery} />
    </div>
  )
}

IntegralQuery.propTypes = {
  dispatch: PropTypes.func.isRequired,
  ruleManage: PropTypes.obj,
  loading: PropTypes.isRequired,
}
export default connect(({ ruleManage }) => ({ ruleManage }))(IntegralQuery)
