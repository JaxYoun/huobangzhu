import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import RuleQuery from './ruleQuery'
import RuleList from './ruleList'

const RuleConfigure = ({
  dispatch,
  ruleManage,
  loading,
}) => {
  const { ruleConfigure } = ruleManage
  return (
    <div>
      <RuleQuery dispatch={dispatch} {...ruleConfigure} />
      <RuleList dispatch={dispatch} {...ruleConfigure} loading={loading} />
    </div>
  )
}

RuleConfigure.propTypes = {
  dispatch: PropTypes.func.isRequired,
  ruleManage: PropTypes.obj,
  loading: PropTypes.isRequired,
}
export default connect(({ ruleManage, loading }) => ({ ruleManage, loading }))(RuleConfigure)
