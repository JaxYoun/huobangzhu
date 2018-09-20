import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import ExamineList from './examineList'
import ExamineQuery from './examineQuery'

const UserExamine = ({
  dispatch,
  userExamine,
}) => {
  return (
    <div>
      <ExamineQuery dispatch={dispatch} {...userExamine} />
      <ExamineList dispatch={dispatch} {...userExamine} />
    </div>
  )
}

UserExamine.propTypes = {
  dispatch: PropTypes.func.isRequired,
  userExamine: PropTypes.obj,
}
export default connect(({ userExamine }) => ({ userExamine }))(UserExamine)
