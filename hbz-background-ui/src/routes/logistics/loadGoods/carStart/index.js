import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import StartList from './startList'
import StartQuery from './startQuery'
const CarStart = ({
  dispatch,
  carStart,
}) => {
  return (
    <div>
      <StartQuery {...carStart} dispatch={dispatch} />
      <StartList {...carStart} dispatch={dispatch} />
    </div>
  )
}

CarStart.propTypes = {
  dispatch: PropTypes.func.isRequired,
  carStart: PropTypes.obj,
}
export default connect(({ carStart }) => ({ carStart }))(CarStart)
