import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import ExamineList from './examineList'
import ExamineQuery from './examineQuery'


const StorageExamine = ({
  dispatch,
  storageExamine,
}) => {
  return (
    <div>
      <ExamineQuery dispatch={dispatch} {...storageExamine} />
      <ExamineList dispatch={dispatch} {...storageExamine} />
    </div>
  )
}

StorageExamine.propTypes = {
  dispatch: PropTypes.func.isRequired,
  storageExamine: PropTypes.obj,
}
export default connect(({ storageExamine }) => ({ storageExamine }))(StorageExamine)
