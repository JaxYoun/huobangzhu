import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import CarList from './carList'
import CarQuery from './carQuery'


const CarManagement = ({
  dispatch,
  carManagement,
}) => {
  return (
    <div>
      <CarQuery dispatch={dispatch} {...carManagement} />
      <CarList dispatch={dispatch} {...carManagement} />
    </div>
  )
}

CarManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  carManagement: PropTypes.obj,
}
export default connect(({ carManagement }) => ({ carManagement }))(CarManagement)
